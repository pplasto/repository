package com.cai.common.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cai.common.define.EGoldOperateType;

/**
 * 每日账号数据统计
 * 
 * @author run
 *
 */

@Document(collection = "every_day_account")
public class EveryDayAccountModel implements Serializable {

	/**
	 * 日期
	 */
	@Indexed(name = "index_notes_date", unique = true)
	private Integer notes_date;

	/**
	 * 用户总数
	 */
	private int account_count;

	/**
	 * 当天注册数量
	 */
	private int register;

	/**
	 * 最高在线人数
	 */
	private int hight_online;

	/**
	 * 活跃账号数量
	 */
	private int active_account_num;

	private String msg;

	/**
	 * 开房间
	 */
	private int open_room;
	/**
	 * 后台加卡
	 */
	private int oss_add;
	/**
	 * 后台减少卡
	 */
	private int oss_dec;
	/**
	 * 代理赠送
	 */
	private int proxy_give;
	/**
	 * 商店购买
	 */
	private int shop_pay;
	/**
	 * 中心充值
	 */
	private int center_pay;

	/**
	 * 记录创建时间
	 */
	private Date create_time;
	
	/**
	 * 麻将牌局详情统计
	 */
	private String mjDetail;
	
	/**
	 * 麻将总牌局
	 * @return
	 */
	public int mjTotal;

	public Integer getNotes_date() {
		return notes_date;
	}

	public void setNotes_date(Integer notes_date) {
		this.notes_date = notes_date;
	}

	public int getAccount_count() {
		return account_count;
	}

	public void setAccount_count(int account_count) {
		this.account_count = account_count;
	}

	public int getRegister() {
		return register;
	}

	public void setRegister(int register) {
		this.register = register;
	}

	public int getHight_online() {
		return hight_online;
	}

	public void setHight_online(int hight_online) {
		this.hight_online = hight_online;
	}

	public int getActive_account_num() {
		return active_account_num;
	}

	public void setActive_account_num(int active_account_num) {
		this.active_account_num = active_account_num;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getOpen_room() {
		return open_room;
	}

	public void setOpen_room(int open_room) {
		this.open_room = open_room;
	}

	public int getOss_add() {
		return oss_add;
	}

	public void setOss_add(int oss_add) {
		this.oss_add = oss_add;
	}

	public int getOss_dec() {
		return oss_dec;
	}

	public void setOss_dec(int oss_dec) {
		this.oss_dec = oss_dec;
	}

	public int getProxy_give() {
		return proxy_give;
	}

	public void setProxy_give(int proxy_give) {
		this.proxy_give = proxy_give;
	}

	public int getShop_pay() {
		return shop_pay;
	}

	public void setShop_pay(int shop_pay) {
		this.shop_pay = shop_pay;
	}

	public int getCenter_pay() {
		return center_pay;
	}

	public void setCenter_pay(int center_pay) {
		this.center_pay = center_pay;
	}

	public void setGoldValue(EGoldOperateType type, int total) {
		if (type == EGoldOperateType.OPEN_ROOM) {
			open_room = total;
		} else if (type == EGoldOperateType.OSS_OPERATE) {
			oss_add = total;
		} else if (type == EGoldOperateType.PROXY_GIVE) {
			proxy_give = total;
		} else if (type == EGoldOperateType.SHOP_PAY) {
			shop_pay = total;
		} else if (type == EGoldOperateType.PAY_CENTER) {
			center_pay = total;
		} else if (type == EGoldOperateType.OSS_OPERATE_DEC) {
			oss_dec = total;
		}
	}

	/**
	 * 增加的房卡总数
	 * @return
	 */
	public int getAdd_card_count() {
		return oss_add+shop_pay+center_pay;
	}
	/**
	 * 减少的房卡总数
	 * @return
	 */
	public int getDec_card_count() {
		return open_room+oss_dec;
	}

	public int getMjTotal() {
		return mjTotal;
	}

	public void setMjTotal(int mjTotal) {
		this.mjTotal = mjTotal;
	}

	public String getMjDetail() {
		return mjDetail;
	}

	public void setMjDetail(String mjDetail) {
		this.mjDetail = mjDetail;
	}

}
