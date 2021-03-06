package com.cai.common.constant.game.mj;


public class Constants_MJ_HLKDD {
    
	public static final int GAME_RULE_BOTTOM_SCORE_02 			= 1; //  0.2分
	public static final int GAME_RULE_BOTTOM_SCORE_05 			= 2; // 0.5分
	public static final int GAME_RULE_BOTTOM_SCORE_1 			= 3; // 1分
	public static final int GAME_RULE_NO_MOUSE 					= 4; // 无耗子
	public static final int GAME_RULE_CATCH_MOUSE 				= 5; // 捉耗子
	public static final int GAME_RULE_WIN_MOUSE 				= 6;// 风耗子
	public static final int GAME_RULE_BU_DAI_FENG 				= 7; // 去风牌
	public static final int GAME_RULE_JIAN_ZI_HU_60 			= 8; // 见字胡60分
	public static final int GAME_RULE_JIAN_ZI_HU_ONLY_ZI_MO 	= 9; // 见字胡仅自摸
	public static final int GAME_RULE_GUO_HU_ONLY_ZI_MO 		= 10; // 过胡仅自摸
	public static final int GAME_RULE_DAI_ZHUANG 				= 11; // 带庄
	public static final int GAME_RULE_FENG_ZUI_ZI 				= 12; // 风嘴子
	public static final int GAME_RULE_QING_YI_SE 				= 13; // 清一色
	public static final int GAME_RULE_YI_TIAO_LONG 				= 14; // 一条龙
	public static final int GAME_RULE_FOUR_MOUSE_HAS_PRIZE_5 	= 15; // 四耗子有喜5分
	public static final int GAME_RULE_FOUR_MOUSE_HAS_PRIZE_10 	= 16; // 10分
	public static final int GAME_RULE_FOUR_MOUSE_HAS_PRIZE_20 	= 17; // 20分
	
	
	public static final int CARD_ESPECIAL_TYPE_HIDE= -2;//报听后打出那张牌 需要扑倒 不让别人看见
	
    
    public static final int HU_CARD_TYPE_ZI_MO = 8; // 自摸
    public static final int HU_CARD_TYPE_DIAN_PAO = 9; // 点炮胡
    public static final int HU_CARD_TYPE_QIANG_GANG_HU = 10; // 抢杠胡
    public static final int HU_CARD_TYPE_BAO_TING = 11; // 报听
    
    public static final int CHR_ZI_MO 				= 0x0001; // 自摸胡
    public static final int CHR_FANG_PAO 			= 0x0002; // 放炮
    public static final int CHR_QIANG_GANG 			= 0x0004; // 抢杠胡
    public static final int CHR_PING_HU 			= 0x0008; // 平胡
    public static final int CHR_QING_YI_SE 			= 0x0010; // 清一色
    public static final int CHR_YI_TIAO_LONG 		= 0x0020; // 一条龙
    public static final int CHR_QI_XIAO_DUI 		= 0x0040; // 七小对
    public static final int CHR_HAO_HUA_QI_XIAO_DUI = 0x0080; // 豪华七小对
    public static final int CHR_DIAN_PAO 			= 0x0100; // 点炮
    public static final int CHR_SHI_SAN_YAO 		= 0x0200; // 十三幺
    public static final int CHR_BEI_QIANG_GANG 		= 0x0400; // 被抢杠
    public static final int CHR_MOUSE_DIAO_CARD		= 0x0800; // 耗子吊牌
    
    
    
 	public static final int CARD_DATA_NOT_DAI_FENG[] = new int[] { 
		0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, // 万子
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
 	};
 	
 	public static final int CARD_DATA_DAI_FENG[] = new int[] { 
		0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, // 万子
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
		0x32, 0x32, 0x32, 0x32, // 南风
		0x33, 0x33, 0x33, 0x33, // 西风
		0x34, 0x34, 0x34, 0x34, // 北风
		0x35, 0x35, 0x35, 0x35, // 红中
		0x36, 0x36, 0x36, 0x36, // 绿发
		0x37, 0x37, 0x37, 0x37, // 白板
	};
}
