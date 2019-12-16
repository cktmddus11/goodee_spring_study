package logic;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class Item {
	private String id;
	@NotEmpty(message="상품명을 입력하세요")  // empty 문자열 인식
	private String name;
	//@NotNull(message="가격을 입력하세요") //  무조건 들어와야됨
	@Min(value=10, message="10원 이상 가능합니다.") // 기본값이 0이니까 이걸로 처리하는게 맞고 notnull은 별로 의미없음
	@Max(value=100000, message="10만원 이하만 가능합니다." )
	private int price;
	@NotEmpty(message="상품 설명을 입력하세요")
	@Size(min=10, max=15, message="10이상 15이하 가능")
	private String description;
	private String pictureUrl;
	private MultipartFile picture; // 업로드된 파일의 내용을 저장함.
	//xml에 설정해둬서 request파라미터로 원래는 안되는데
	// spring에서 xml설정 때문에 가능하게 함??
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	public MultipartFile getPicture() {
		return picture;
	}
	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}
	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description
				+ ", pictureUrl=" + pictureUrl + ", picture=" + picture + "]";
	}
	
	
	
}
