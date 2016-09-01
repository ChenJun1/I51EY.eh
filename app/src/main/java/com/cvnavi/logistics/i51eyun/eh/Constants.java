package com.cvnavi.logistics.i51eyun.eh;

import java.util.ArrayList;


/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 常量定义类，系统所需常量，尽量在该类定义
 * @date 2016-5-17 下午1:30:08
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class Constants {

	// 服务端API
	//12.58  yebing
	//11.178  wanwei 
	//12.160 liuzhuhui
//	public static final String URL = "http://gms.i51ey.com:48080/";
	public static final String URL = "http://gms.i51ey.com:8282/";//三期地址

//	public static final String URL = "http://10.10.12.58:8080/";
//s	public static final String URL = "http://10.10.12.124:8080/";
//	public static final String URL = "http://10.10.12.180:8080/";
//	public static final String URL = "http://10.10.11.100:8080/";
//	public static final String URL = "http://10.10.11.124:8080/";
 //  public static final String URL = "http://10.10.12.175:8080/";
	
//	public static final String ReceiveGPS_URL = "http://10.10.12.180:8080/";
//	public static final String ReceiveGPS_URL = "http://10.10.12.131:8080/";
//	public static final String ReceiveGPS_URL = "http://10.10.12.124:8080/";
	public static final String ReceiveGPS_URL = "http://gms.i51ey.com:48080/";
	
	public static final String CarPosition_URL = "http://10.10.11.124:8083/api/lps/v2/CarPosition/";
	
	public static final String ReceiveGPSData_URL = ReceiveGPS_URL + "GPSService/ReceiveGPSData";

	public static final String Login_URL = URL + "LoginService/UserPhoneLogin";
	public static final String OutLogin_URL = URL + "LoginService/OutLogin";
	public static final String VerifyCode_URL = URL + "LoginService/VerifyCode";
	public static final String RequestJPushID_URL = URL+ "LoginService/RequestJPushID";

	public static final String Get_UserInfo_URL = URL+ "UserService/GetUserInfo";
	public static final String Add_UserInfo_URL = URL+ "LoginService/UserPhoneReg";
	public static final String Modify_UserInfo_URL = URL+ "UserService/UpdateUserInfo";

	public static final String Get_DeriverList_URL = URL+ "UserService/GetDeriverList";
	public static final String Create_DeriverList_URL = URL+ "UserService/CreateDeriverList";
	public static final String Delete_DeriverList_URL = URL+ "UserService/DeleteDeriverList";

	public static final String Get_ParkList_URL = URL+ "LoginService/GetParkList";
	public static final String Get_CarTypeList_Url = URL+ "BusinessService/GetDicList";
	public static final String Get_CarSizeList_Url = URL+ "BusinessService/GetDicList";
	public static final String Get_TransportAreaList_Url = URL+ "BusinessService/GetDicList";
	public static final String Get_ConsignorTypeList_Url = URL+ "BusinessService/GetDicList";
	public static final String Get_GoodsShapeList_Url = URL+ "BusinessService/GetDicList";
	public static final String Get_GoodsTypeList_Url = URL+ "BusinessService/GetDicList";

	//==============================================无用的URL==============================================
	// 司机确认完成订单
//		public static final String ConfirmCompleteOrderByDriver_Url = URL+ "BusinessService/ConfirmCompleteOrderByDriver";
	// 获取发货订单详情（修改订单拉取）
//	public static final String Get_SpecificOrderInfo_Url = URL+ "BusinessService/GetSpecificOrderInfo";
	//=====================================================================================================
	
	//=============================================无用的参数===============================================
	public static boolean updata = false;
	//=====================================================================================================
	
	
	// 获取简单车辆信息列表
	public static final String Get_simpleCarListByCondition_URL = URL+ "BusinessService/GetsimpleCarListByCondition";
	// 获取车辆详细信息
	public static final String Get_CarMsgListByCondition_URL = URL+ "BusinessService/GetCarMsgListByCondition";
	// 获取常用收货人
	public static final String Get_ReceiverList_URL = URL+ "UserService/GetReceiverList";

	public static final String Get_CarByCode_Url = URL+ "LoginService/GetCarByCode";
	public static final String Get_CarList_ByCondition_Url = URL+ "BusinessService/GetCarListByCondition";

	// 添加发货订单
	public static final String Add_Order_Url = URL + "BusinessService/AddOrder";
	// 司机订单详情
	public static final String Get_WaitingOrderList_Url = URL+ "BusinessService/GetWaitingOrderList";
	// 司机接取订单
	public static final String Receive_WaitingOrder_Url = URL+ "BusinessService/ReceiveWaitingOrder";
	// 拒绝接单/货主取消指定
	public static final String Refuse_HadCarOrder_Url = URL+ "BusinessService/RefuseOrCancel_HadCarOrder";
	// 获取待接订单详情
	public static final String OrderInfo_Url = URL+ "BusinessService/GetOrderInfo";
	// 订单列表
	public static final String Order_Url = URL+ "BusinessService/GetOrderBaseList";
	// 修改订单
	public static final String ModifyOrder_Url = URL+ "BusinessService/ModifyOrder";
	// 删除
	public static final String DeleteOrder_Url = URL+ "BusinessService/DeleteOrder";
	
	// 货主确定完成订单
	public static final String ConfirmCompleteOrderByConsignor_Url = URL+ "BusinessService/ConfirmCompleteOrderByConsignor";

	// 我要找货
	public static final String LookupWaitingOrder = URL+ "BusinessService/LookupWaitingOrder";
	// 获取首页十条数据
	public static final String Get_HomepageList_Url = URL+ "BusinessService/AppGetHomepageList";

	// 上下班
	public static final String OnWork_OffWork_Url = URL+ "LoginService/OnAndOffWork";
	// 获取订单全部详情信息
	public static final String Get_SpecificOrderInfoBySerial_Oid_Url = URL+ "BusinessService/GetSpecificOrderInfoBySerial_Oid";
	
	
	public static String TransportProtocol_URL = "http://imgserver.i51ey.com:48181/index.html";
	
	//=================================二期========================================
	// 帮助
	public static String HelpManual_URL = "http://imgserver.i51ey.com:48181/help.html";
	
	// 常用线路查询
	public static final String Get_LineList_Url = URL+ "UserService/GetLineList";
	// 添加常用线路
	public static final String Create_Line_Url = URL+ "UserService/CreateLine";
	// 删除常用线路
	public static final String Delete_Line_Url = URL+ "UserService/DeleteLine";
	//设置首选路线
	public static final String Update_Line_Url = URL+ "UserService/UpdateLine";
	
	//添加常用联系人
	public static final String Add_FavoriteContacts_URL = URL+ "UserService/AddFavoriteContacts";
	//查询常用联系人
	public static final String Get_FavoriteContactsList_URL = URL+ "UserService/GetFavoriteContactsList";
	//删除常用联系人
	public static final String Del_FavoriteContacts_URL = URL+ "UserService/DelFavoriteContacts";
	//历史订单
	public static final String Get_OrderBaseListByHistory_URL = URL+ "BusinessService/GetOrderBaseListByHistory";
	//添加客户留言
	public static final String Add_UserMessage_URL = URL+ "UserService/AddUserMessage";
	//车辆跟踪列表
	public static final String CarTrackList_URL = URL+ "BusinessService/CarTrackList";
	//评价提交
	public static final String OrderEvaluation_URL = URL+ "BusinessService/OrderEvaluation";
	//车辆位置
	public static final String CarTrack_Detailed_URL = URL+ "BusinessService/CarTrackDetailed";
	//货主我的评价
	public static final String Get_OrderEvaluationList_URL = URL+ "BusinessService/GetOrderEvaluationList";
	//评价信息
	public static final String Get_OrderEvaluationInfo_URL = URL+ "BusinessService/GetOrderEvaluationInfo";
	//获取未读信息数量(角标)
	public static final String GETISREADORDERROW_URL = URL+ "BusinessService/GetIsReadOrderRow";
	
	//动态数据字典
    public static final String Get_DicAndSetData_URL = URL+ "DicDataService/GetDicAndSetData";
	
    // 司机已接运单取消接单
  	public static final String Set_CanselOrderInfo_URL = URL+ "BusinessService/SetCanselOrderInfo";
  	// 货主已接运单取消发货
  	public static final String CancelOrder_URL = URL+ "BusinessService/CancelOrder";
	
	// 司机提货上传照片（评论，提货完成）
  	public static final String Confirm_TakeOrCompleteOrderByDriver_URL = URL+ "BusinessService/ConfirmTakeOrCompleteOrderByDriver";
  
  	//获取版本号
  	public static final String GetAppVersion_URL = URL+ "LoginService/GetAppVersion";
  	//获取轮播图片
  	public static final String GetAdvertPic_URL = URL+ "UserService/GetAdvertPic";
  	//验证七天免登陆Token
  	public static final String LoginByValidToken = URL+ "LoginService/LoginByValidToken";
  	//在线升级下载
  	public static final String GetAppDownPath_URL = URL+ "LoginService/GetAppDownPath";
  	
  	
  	
  	
  	
  	
	public static final int DIALOG_CONFRIM = 0;
	public static final int DIALOG_TIP = 1;

	public static final int Refresh = 2;
	public static final int LoadMore = 3;

	public static final int Request_Success = 4;
	public static final int Request_Fail = 5;
	public static final int Result_Success = 6;
	public static final int Result_Fail = 7;
	public static final int Result_cancelTime = 8;
	public static final int Request_Success_load = 9;
	
	public static final int Request_Delete=10;
	public static final int Request_NoData=11;
	public static final int Request_cancelOrder=12;
	public static final int Request_collection = 13;
	public static final int Data_Error = 14;
	

	
	public static final int ChangeWorkStatus_Success = 15;
	public static final int ChangeWorkStatus_Fail = 16;
	public static final int Request_OrderCome=17;
	public static final int OrderRow_Success=19;
	public static final int Order_jiaojuo_Success=20;
	
	public static final int Request_RejectOrder = 21;
	public static final int Request_ReceivedCancelOrder = 22;
	
	public static final int Asses_Success = 23;
	public static final int Asses_Fail = 24;
	public static final int Request_False = 25;
	
	public static final int Request_LoadImg = 26;
	public static final int Request_LoadImgFalse = 27;
	
	public static final int Delete_Success=18;
	public static final int Update_Success=28;
	
	//=========================sp保存key======================
	public static final String USERKEY_STRING="USERKEY_STRING";
	public static final String USERTOKEN_STRING="USERTOKEN_STRING";
	public static final String USERTEL_STRING="USERTEL_STRING";
	public static final String USERTYPE_STRING="USERTYPE_STRING";
	public static final String USERCOMPANY_STRING="USERCOMPANY_STRING";
	
	//==============================分页=================================
	public static final int FindStartNum=1;
	public static final int FindNum=10;

	
	public static final String ProvinceName = "ProvinceName";
	public static final String CityName = "CityName";

	public static final String OrderKey = "OrderKey";
	public static final String OrderInfo = "OrderInfo";

	public static final String DeliveryGoods_FindCar = "FindCar";

	public static final String Region_TAG = "RegionTAG";
	public static final String FAHUO_TAG = "Fahuo";
	public static final String SHOUHUO_TAG = "Shouhuo";
	public static final String FOLLOWFA_TAG = "FOLLOWFA_TAG";
	public static final String FOLLOWSHOU_TAG = "FOLLOWSHOU_TAG";
	public static final String ADDCOMMONCONSIGNEE = "ADDCOMMONCONSIGNEE";
	public static final String RegionKey = "region_extra";
	public static final String Province_City_Key = "province_city_extra";
	public static final String Province_Key = "province_extra";
	
	public static final String CarDetail_Info = "CarDetail_Info";
	public static final String commonConsignee = "commonConsignee";
	
	public static final String JPushOrderKey = "JPushOrderKey";
	public static final String AssessKey = "asses_extra";
	public static final String Image_pic = "imagePic";
	
	public static final String ISPCA = "Province_City_area";//省市区必选
	
	public static final int BLUE_COLOR=0xFF3AAFDA;
	
	public static Boolean isLogin = false;
	
	public static String errorMsg="网络异常，请稍后再试！";
	public static String NoDataMsg="当前没有查询到信息！";
	public static String LoginerrorMsg="验证码或账号错误，请重新输入！";
	
	//首页广告图
	public static final ArrayList<String> mImageUrl =new ArrayList<String>();
	
	public static  String imageUrl1 = "http://imgserver.i51ey.com:48181/gmsimg/common/1.png";
	public static  String imageUrl2 = "http://imgserver.i51ey.com:48181/gmsimg/common/2.png";
	public static String imageUrl3 = "http://10.10.11.124:8181/ADVIMG/2.png";
	
	//引导页资源
	public static final int[] pics = {R.drawable.guid_tow,R.drawable.guid_tree,R.drawable.guid_four}; 
	
	//更新apk的大小
	public static final int apkSize=8644524;
}
