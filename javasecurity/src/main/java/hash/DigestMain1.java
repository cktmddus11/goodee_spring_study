package hash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestMain1 {
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		byte[] plain = null;
		byte[] hash = null;
		String[] algo = {"MD5", "SHA-1", "SHA-256", "SHA-512"};
		System.out.println("해쉬값을 구할 문자열을 입력하세요");
		plain = br.readLine().getBytes();
		System.out.println("평문 : "+new String(plain));
		for(String al : algo) {
			// 해시알고리즘
			MessageDigest md = MessageDigest.getInstance(al);
			hash = md.digest(plain); // MD5 행태의 다이제스트(압축?)로 변환?
			// 알고리즘 끝
			System.out.println(al+"해쉬값 크기 : "+(hash.length * 8)+"bits");
			System.out.print("해쉬값 : ");
			for(byte b : hash) System.out.printf("%02X", b);
			System.out.println();
		}
	}
}