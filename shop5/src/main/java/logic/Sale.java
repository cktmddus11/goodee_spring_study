package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
	private int saleid;
	private String userid;
	private Date updatetime;
	private User user;
	private List<SaleItem> itemList = new ArrayList<SaleItem>();
	
	public long getTotal() {
		long sum = 0;
		for(SaleItem si : itemList) {
			Item item = si.getItem();
			sum += si.getQuantity() * item.getPrice();
		}
		return sum;
	}

	public int getSaleid() {
		return saleid;
	}
	public void setSaleid(int saleid) {
		this.saleid = saleid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<SaleItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleItem> itemList) {
		this.itemList = itemList;
	}

	@Override
	public String toString() {
		return "Sale [saleid=" + saleid + ", userid=" + userid + ", updatetime=" + updatetime + ", user=" + user
				+ ", itemList=" + itemList + "]";
	}
	
	
}