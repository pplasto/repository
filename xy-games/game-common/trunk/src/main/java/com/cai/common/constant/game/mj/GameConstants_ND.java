package com.cai.common.constant.game.mj;


import com.cai.common.constant.GameConstants;

public class GameConstants_ND extends GameConstants {

	public static final int GAME_RULE_PLAYER_4 = 1; // 四人场
	public static final int GAME_RULE_PLAYER_3 = 2; // 三人场
	public static final int GAME_RULE_PLAYER_2 = 3; // 二人场
	public static final int GAME_RULE_JIA_DA = 4; // 加大
	public static final int GAME_RULE_GEN_ZHUANG = 5; // 跟庄
	public static final int GAME_RULE_MA_2 = 6; // 2马
	public static final int GAME_RULE_MA_4 = 7; // 4马
	public static final int GAME_RULE_MA_6 = 8; // 6马
	public static final int GAME_RULE_MA_8 = 9; // 8马
	public static final int GAME_RULE_HAI_DI = 10; //海底捞
	public static final int GAME_RULE_MA_FANG_WEI = 11; //方位马   
	public static final int GAME_RULE_MA_DING_ZHUANG = 12; // 定庄马

	
	public static final int CHR_HENAN_DAN_DIAO = 0x00040000; // 单吊
	
	// 全部的麻将数据
	public static final int CARD_DATA_MAX[] = new int[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, // 万子
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, // 万子
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, // 万子
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, // 万子
			0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, // 索子
			0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, // 索子
			0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, // 索子
			0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, // 索子
			0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, // 同子
			0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, // 同子
			0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, // 同子
			0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, // 同子

			0x31, 0x31, 0x31, 0x31, // 东风
			0x32, 0x32, 0x32, 0x32, // 西风
			0x33, 0x33, 0x33, 0x33, // 南风
			0x34, 0x34, 0x34, 0x34, // 北风
			0x35, 0x35, 0x35, 0x35, // 红中
			0x36, 0x36, 0x36, 0x36, // 绿发
			0x37, 0x37, 0x37, 0x37, // 白板
	};
}