package com.cai.common.util;

import java.util.Date;
import java.util.List;

import com.cai.common.domain.Account;
import com.cai.common.domain.AccountModel;
import com.cai.common.domain.AccountParamModel;
import com.cai.common.domain.AccountRecommendModel;
import com.cai.common.domain.AccountWeixinModel;

import protobuf.redis.ProtoRedis.RsAccountModelResponse;
import protobuf.redis.ProtoRedis.RsAccountParamModelResponse;
import protobuf.redis.ProtoRedis.RsAccountRecommendResponse;
import protobuf.redis.ProtoRedis.RsAccountResponse;
import protobuf.redis.ProtoRedis.RsAccountWeixinModelResponse;

/**
 * 能用redis消息(protobuf)转给model
 * 
 * @author run
 *
 */
public class RedisToModelUtil {

	public static void rsAccountResponseToAccount(RsAccountResponse rsAccountResponse, Account account) {
		AccountModel accountModel = account.getAccountModel();

		if (rsAccountResponse.hasLastGameIndex()) {
			account.setLastGameIndex(rsAccountResponse.getLastGameIndex());
		}
		if (rsAccountResponse.hasLastGameStatus()) {
			account.setLastGameStatus(rsAccountResponse.getLastGameStatus());
		}
		if (rsAccountResponse.hasLastLogicIndex()) {
			account.setLastLogicIndex(rsAccountResponse.getLastLogicIndex());
		}
		if (rsAccountResponse.hasLastProxyIndex()) {
			account.setLastProxyIndex(rsAccountResponse.getLastProxyIndex());
		}
		if (rsAccountResponse.hasGameId()) {
			account.setGame_id(rsAccountResponse.getGameId());
		}
		if(rsAccountResponse.hasRoomId()){
			account.setRoom_id(rsAccountResponse.getRoomId());
		}
		

		if (rsAccountResponse.hasRsAccountModelResponse()) {
			RsAccountModelResponse rsAccountModelResponse = rsAccountResponse.getRsAccountModelResponse();
			if (rsAccountModelResponse.hasPassword()) {
				accountModel.setPassword(rsAccountModelResponse.getPassword());
			}
			if (rsAccountModelResponse.hasLoginTimes()) {
				accountModel.setLogin_times(rsAccountModelResponse.getLoginTimes());
			}
			if (rsAccountModelResponse.hasLastLoginTime()) {
				accountModel.setLast_login_time(new Date(rsAccountModelResponse.getLastLoginTime()));
			}
			if (rsAccountModelResponse.hasMobilePhone()) {
				accountModel.setMobile_phone(rsAccountModelResponse.getMobilePhone());
			}
			if (rsAccountModelResponse.hasHistoryPayGold()) {
				accountModel.setHistory_pay_gold(rsAccountModelResponse.getHistoryPayGold());
			}
			if (rsAccountModelResponse.hasGold()) {
				accountModel.setGold(rsAccountModelResponse.getGold());
			}
			if (rsAccountModelResponse.hasClientIp()) {
				accountModel.setClient_ip(rsAccountModelResponse.getClientIp());
			}
			if(rsAccountModelResponse.hasTodayOnline()){
				accountModel.setToday_online(rsAccountModelResponse.getTodayOnline());
			}
			if(rsAccountModelResponse.hasHistoryOnline()){
				accountModel.setHistory_online(rsAccountModelResponse.getHistoryOnline());
			}
			if(rsAccountModelResponse.hasBanned()){
				accountModel.setBanned(rsAccountModelResponse.getBanned());
			}
			if(rsAccountModelResponse.hasIsAgent()){
				accountModel.setIs_agent(rsAccountModelResponse.getIsAgent());
			}
			if(rsAccountModelResponse.hasLastClientFlag()){
				accountModel.setLast_client_flag(rsAccountModelResponse.getLastClientFlag());
			}
			if(rsAccountModelResponse.hasClientVersion()){
				accountModel.setClient_version(rsAccountModelResponse.getClientVersion());
			}
			if(rsAccountModelResponse.hasRecommendId()){
				accountModel.setRecommend_id(rsAccountModelResponse.getRecommendId());
			}
			if(rsAccountModelResponse.hasMoney()){
				accountModel.setMoney(rsAccountModelResponse.getMoney());
			}
			if(rsAccountModelResponse.hasClientIp2()){
				accountModel.setClient_ip2(rsAccountModelResponse.getClientIp2());
			}
			
			if(rsAccountModelResponse.hasNeedDb()){
				accountModel.setNeedDB(rsAccountModelResponse.getNeedDb());
			}
		}

		if (rsAccountResponse.hasRsAccountWeixinModelResponse()) {

			AccountWeixinModel accountWeixinModel = account.getAccountWeixinModel();
			RsAccountWeixinModelResponse wx = rsAccountResponse.getRsAccountWeixinModelResponse();
			if (wx.hasAccessToken()) {
				accountWeixinModel.setAccess_token(wx.getAccessToken());
			}
			if (wx.hasRefreshToken()) {
				accountWeixinModel.setRefresh_token(wx.getRefreshToken());
			}
			if (wx.hasOpenid()) {
				accountWeixinModel.setOpenid(wx.getOpenid());
			}
			if (wx.hasScope()) {
				accountWeixinModel.setScope(wx.getScope());
			}
			if (wx.hasUnionid()) {
				accountWeixinModel.setUnionid(wx.getUnionid());
			}
			if (wx.hasNickname()) {
				accountWeixinModel.setNickname(wx.getNickname());
			}
			if (wx.hasSex()) {
				accountWeixinModel.setSex(wx.getSex());
			}
			if (wx.hasProvince()) {
				accountWeixinModel.setProvince(wx.getProvince());
			}
			if (wx.hasCity()) {
				accountWeixinModel.setCity(wx.getCity());
			}
			if (wx.hasCountry()) {
				accountWeixinModel.setCountry(wx.getCountry());
			}
			if (wx.hasHeadimgurl()) {
				accountWeixinModel.setHeadimgurl(wx.getHeadimgurl());
			}
			if (wx.hasPrivilege()) {
				accountWeixinModel.setPrivilege(wx.getPrivilege());
			}
			if (wx.hasLastFlushTime()) {
				accountWeixinModel.setLast_flush_time(new Date(wx.getLastFlushTime()));
			}
			if (wx.hasSelfToken()) {
				accountWeixinModel.setSelf_token(wx.getSelfToken());
			}
			if (wx.hasLastFalseSelfToken()) {
				accountWeixinModel.setLast_false_self_token(new Date(wx.getLastFalseSelfToken()));
			}
			if(wx.hasNeedDb()){
				accountWeixinModel.setNeedDB(wx.getNeedDb());
			}
		}
		
		//账号参数
		if(rsAccountResponse.getRsAccountParamModelResponseListCount()>0){
			List<RsAccountParamModelResponse> paramModelList = rsAccountResponse.getRsAccountParamModelResponseListList();
			for(RsAccountParamModelResponse m : paramModelList){
				
				AccountParamModel accountParamModel = account.getAccountParamModelMap().get(m.getType());
				if(accountParamModel!=null){
					if(m.hasVal1())
						accountParamModel.setVal1(m.getVal1());
					if(m.hasStr1())
						accountParamModel.setStr1(m.getStr1());
					if(m.hasLong1())
						accountParamModel.setLong1(m.getLong1());
					if(m.hasNeedDb())
						accountParamModel.setNeedDB(m.getNeedDb());
					if(m.hasData1())
						accountParamModel.setDate1(new Date(m.getData1()));
				}else{
					//新加的值，标识一下
					accountParamModel = new AccountParamModel();
					accountParamModel.setAccount_id(account.getAccount_id());
					accountParamModel.setType(m.getType());
					accountParamModel.setNeedDB(false);
					accountParamModel.setVal1(m.getVal1());
					accountParamModel.setStr1(m.getStr1());
					accountParamModel.setLong1(m.getLong1());
					accountParamModel.setDate1(new Date(m.getData1()));
					accountParamModel.setNewAddValue(true);
					account.getAccountParamModelMap().put(accountParamModel.getType(), accountParamModel);
				}
				
			}
		}
		
		//玩家邀请列表
		if(rsAccountResponse.getRsAccountRecommendResponseListCount()>0){
		    List<RsAccountRecommendResponse> recommentdList = rsAccountResponse.getRsAccountRecommendResponseListList();
		    for(RsAccountRecommendResponse m : recommentdList){
		    	AccountRecommendModel accountRecommendModel = account.getAccountRecommendModelMap().get(m.getTargetAccountId());
		    	if(accountRecommendModel==null){
		    		accountRecommendModel = new AccountRecommendModel();
		    		accountRecommendModel.setAccount_id(m.getAccountId());
		    		accountRecommendModel.setTarget_account_id(m.getTargetAccountId());
		    		accountRecommendModel.setCreate_time(new Date(m.getCreateTime()));
		    		accountRecommendModel.setGold_num(m.getGoldNum());
		    		accountRecommendModel.setTarget_name(m.getTargetName());
		    		accountRecommendModel.setTarget_icon(m.getTargetIcon());
		    		account.getAccountRecommendModelMap().put(accountRecommendModel.getTarget_account_id(), accountRecommendModel);
		    	}
		    	//没有更新的，不用处理
		    }
		}
		
		
	}

}
