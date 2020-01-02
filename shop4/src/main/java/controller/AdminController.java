package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import exception.LoginException;
import logic.Mail;
import logic.ShopService;
import logic.User;
/* 
 * AdminController 클래스는 모든 메서드는 admin으로 로그인 한 경우만 처리가 가능함
 * 1. 로그인 안된 경우 : 로그인 후 이용 가능합니다. login.shop페이지로 이동
 * 2. 관리자 로그인이 아닌 경우 : 관리자만 가능한 거래입니다. main.shop 페이지 이동 
 * => AdminAspect 클래스 구현하기
 * 
 * */
@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list")
	public ModelAndView list(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<User> list = service.userList();
		mav.addObject("list", list);
		return mav;
	}
	@RequestMapping("mailForm")
	public ModelAndView mainForm(String [] idchks, HttpSession session) {
		ModelAndView mav = new ModelAndView("admin/mail");
		if(idchks == null || idchks.length == 0) {
			throw new LoginException("메일을 보낼 대상자를 선택하세요", "list.shop");
		}
		List<User> list = service.userList(idchks);
		mav.addObject("list", list);
		return mav;
	}
	
	@RequestMapping("mail")
	public ModelAndView mail(Mail mail, HttpSession session) {  // aop검증받음
		ModelAndView mav = new ModelAndView("/alert");
		mailSend(mail);
		mav.addObject("msg", "메일전송이 완료되었습니다.");
		mav.addObject("url", "list.shop"); 
		return mav;
	}
	private final class MyAuthenticator extends Authenticator{ 
		// private 다른 클래스에서 접근 불가능 -> 객체 생성 불가
		// final 상속 불가능
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id,  pw);
		}
	}
	private void mailSend(Mail mail) {
		MyAuthenticator auth = new MyAuthenticator(mail.getNaverid(), mail.getNaverpw());
		// 메일 전송 환경 설정
		// resources/mail.properties 파일로 생성해서 하는게 더 좋을수도 ?
		Properties prop = new Properties();
		
		  try{
			  FileInputStream fis = new FileInputStream("C:\\Users\\GDJ_19\\Desktop\\git3\\shop3\\src\\main\\resources\\mail.properties"); 
			  prop.load(fis);
			  prop.put("mail.smtp.user", mail.getNaverid());
		  }catch(IOException e){
		  		e.printStackTrace();
		  }
		
//		  prop.put("mail.smtp.host", "smtp.naver.com");
//		  prop.put("mail.smtp.starttls.enable","true"); prop.put("mail.debug", "true");
//		  prop.put("mail.smtp.auth", "true"); prop.put("mail.smtp.port", "465");
//		  prop.put("mail.smtp.user", mail.getNaverid());
//		  prop.put("mail.smtp.socketFactory.port", "465");
//		 prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//		 prop.put("mail.smtp.socketFactory.fallback", "false");
		 
		Session session = Session.getInstance(prop, auth);
		// 하나의 메일 객체(content, 첨부파일1, 첨부파일2);
		MimeMessage mimeMsg = new MimeMessage(session);
		try {
			mimeMsg.setFrom(new InternetAddress(mail.getNaverid()+"@naver.com")); // 보내는 사람 설정
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			String [] emails = mail.getRecipient().split(",");
			// 받는 사람들 
			for(String email : emails) {
				try {
					// 받는 사람 이름 한글 인코딩
					/* 
					 * new String(email.getBytes("utf-8"), "8859_1")
					 * email.getBytes("utf-8") : email 문자열을 byte[] 로 형태로 변경
					 * 										- 문자열을 utf-8로 인식
					 * 8859_1 : byte[] 배열을 8859_1 인코딩방식으로 변경
					 * 
					 * */
					addrs.add(new InternetAddress(new String(email.getBytes("utf-8"), "8859_1")));
				}catch(UnsupportedEncodingException ue) {
					ue.printStackTrace();
				}
			}
			InternetAddress[] arr = new InternetAddress[emails.length];
			// arr : 받는 사람들
			for(int i = 0;i<addrs.size();i++) {
				arr[i] = addrs.get(i);
			}
			mimeMsg.setSentDate(new Date());
			mimeMsg.setRecipients(Message.RecipientType.TO, arr);
			mimeMsg.setSubject(mail.getTitle());
			MimeMultipart multipart = new MimeMultipart(); // 내용, 첨부파일?
			MimeBodyPart message = new MimeBodyPart();
			// 내용 부분
			message.setContent(mail.getContents(), mail.getMtype());
			multipart.addBodyPart(message);
			for(MultipartFile mf : mail.getFile1()) { // 첨부파일 한개씩 만듦?
				if((mf != null) && (!mf.isEmpty())) {
					multipart.addBodyPart(bodyPart(mf));
				}
			}
			mimeMsg.setContent(multipart);
			Transport.send(mimeMsg);
		}catch(MessagingException me) {
			me.printStackTrace();
		}
	}
	private BodyPart bodyPart(MultipartFile mf) {
		MimeBodyPart body = new MimeBodyPart();
		String orgFile = mf.getOriginalFilename(); // 첨부된 파일의 이름
		String path = "C:\\Users\\GDJ_19\\Desktop\\mailtest\\"; // 첨부파일 업로드 위치
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}
		File f1 = new File(path + orgFile); // 첨부파일정보
		try {
			mf.transferTo(f1); // 업로드된 파일 내용을 서버의 파일로 생성하기
			body.attachFile(f1);
			body.setFileName(new String(orgFile.getBytes("UTF-8"), "8859_1"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return body;
	}
	
	
}
