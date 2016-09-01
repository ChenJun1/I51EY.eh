package com.cvnavi.logistics.i51eyun.eh.utils;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.DataResultBase;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.FollowCarListRequestBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.MCar;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.car.MDict_CarCode_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.MCollect;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.MDict_FavoriteContacts_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.collection.Orders_Consignee_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.AdvertPicBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_CarSize_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_CarType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_ConsignorType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsShape_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_GoodsType_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.common.Dict_TransportArea_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.commonlyline.MDict_Line_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.evaluation.OrderEvaluationListInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.locationtracking.LocationTrackingBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.park.MDict_Company_Sheet;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.AreaBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.CityBean;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.region.ProvinceBean;
import com.cvnavi.logistics.i51eyun.eh.jpush.bean.JPushMessageBean;
import com.cvnavi.logistics.i51eyun.eh.jpush.bean.JPushOrderMessageBean;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.List;

/**
 * 
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description 数据解析类。 示例： 1，VerifyCodeBean bean =
 *              (VerifyCodeBean)JsonUtils.parseData(jsonObject.toString(),
 *              VerifyCodeBean. class); 2，List<LinkedHashMap<String, Object>>
 *              list = (List<LinkedHashMap<String, Object>>) JsonUtils
 *              .parseDataForList(str); if (list != null) { try { for (int i =
 *              0; i < list.size(); i++) { Map<String, Object> map =
 *              list.get(i); Set<String> set = map.keySet(); for (Iterator
 *              <String> it = set.iterator(); it.hasNext();) { String key =
 *              it.next(); LogUtil.d("-->>carcode:" +
 *              String.valueOf(map.get("CarCode"))); System.out.println(key +
 *              ":" + map.get(key)); } } } catch (Exception e) {
 *              e.printStackTrace(); } }
 * @date 2016-5-17 下午1:26:20
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */
public class JsonUtils {

	public static final ObjectMapper JACKSON_MAPPER = new ObjectMapper().configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	/**
	 * 解析单个对象。
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseData(String jsonStr, Class<?> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);
		return (T) JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(), clazz);
	}

	/**
	 * 解析单个对象。
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseData2(String jsonStr, Class<?> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return (T) JACKSON_MAPPER.readValue(jsonStr, clazz);
	}

	/**
	 * 解析集合对象。（通用）
	 * 
	 * @param <clazz>
	 * @param <T>
	 * 
	 * @param jsonStr
	 * @param type
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<?> parseDataForList(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		return JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(), List.class);
	}

	public static DataResultBase parseDataResultBase(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		DataResultBase bean = (DataResultBase) JACKSON_MAPPER.readValue(jsonStr.trim(), DataResultBase.class);
		return bean;
	}

	/**
	 * 对象转换为JSON数据。
	 * 
	 * @param object
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String toJsonData(Object object) throws JsonGenerationException, JsonMappingException, IOException {
		return JACKSON_MAPPER.writeValueAsString(object);
	}

	public static List<ProvinceBean> parseProvinceList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<ProvinceBean> dataList = JACKSON_MAPPER.readValue(jsonStr.trim(), new TypeReference<List<ProvinceBean>>() {
		});
		return dataList;
	}

	public static List<CityBean> parseCityList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<CityBean> dataList = JACKSON_MAPPER.readValue(jsonStr.trim(), new TypeReference<List<CityBean>>() {
		});
		return dataList;
	}

	public static List<AreaBean> parseAreaList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<AreaBean> dataList = JACKSON_MAPPER.readValue(jsonStr.trim(), new TypeReference<List<AreaBean>>() {
		});
		return dataList;
	}

	/**
	 * 解析园区列表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<MDict_Company_Sheet> parseParkList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<MDict_Company_Sheet> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<MDict_Company_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 解析常用车辆管理
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<MCollect> parseMCollectList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<MCollect> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<MCollect>>() {
				});
		return dataList;
	}

	/**
	 * 解析车辆列表。
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<MCar> parseCarListByConditionList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<MCar> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<MCar>>() {
				});
		return dataList;
	}

	/**
	 * 解析车辆类型列表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Dict_CarType_Sheet> parseCarTypeList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<Dict_CarType_Sheet> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<Dict_CarType_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 解析运输区域列表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Dict_TransportArea_Sheet> parseTransportAreaList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<Dict_TransportArea_Sheet> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<Dict_TransportArea_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 解析尺寸列表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Dict_CarSize_Sheet> parseCarSizeList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<Dict_CarSize_Sheet> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<Dict_CarSize_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 解析常用发货人管理
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Orders_Consignee_Sheet> parseConsigneeList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<Orders_Consignee_Sheet> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<Orders_Consignee_Sheet>>() {
				});
		return dataList;
	}

	public static List<MDict_FavoriteContacts_Sheet> parseFavoriteContactsList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<MDict_FavoriteContacts_Sheet> dataList = JACKSON_MAPPER.readValue(
				String.valueOf(bean.getDataValue()).trim(), new TypeReference<List<MDict_FavoriteContacts_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 解析货物类型列表。
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Dict_GoodsType_Sheet> parseGoodsTypeList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<Dict_GoodsType_Sheet> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<Dict_GoodsType_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 解析货物形状列表。
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Dict_GoodsShape_Sheet> parseGoodsShapeList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<Dict_GoodsShape_Sheet> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<Dict_GoodsShape_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 获取订单列表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<WaitingOrderBaseInfo> parseOrderBeanList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<WaitingOrderBaseInfo> OrderBaseList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<WaitingOrderBaseInfo>>() {
				});
		return OrderBaseList;
	}

	/**
	 * 解析货主类型列表。
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Dict_ConsignorType_Sheet> parseConsigneeTypeList(String jsonStr)
			throws JsonGenerationException, JsonMappingException, IOException {
		DataResultBase bean = parseDataResultBase(jsonStr);

		List<Dict_ConsignorType_Sheet> dataList = JACKSON_MAPPER.readValue(String.valueOf(bean.getDataValue()).trim(),
				new TypeReference<List<Dict_ConsignorType_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 解析推送消息。
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static JPushOrderMessageBean parseJPushMessageBase(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		JPushMessageBean jPushMessageBean = parseJPushMessage(jsonStr);

		JPushOrderMessageBean bean = (JPushOrderMessageBean) JACKSON_MAPPER.readValue(jPushMessageBean.EXTRA_EXTRA,
				JPushOrderMessageBean.class);
		return bean;
	}

	private static JPushMessageBean parseJPushMessage(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		JPushMessageBean bean = (JPushMessageBean) JACKSON_MAPPER.readValue(jsonStr.trim(), JPushMessageBean.class);
		return bean;
	}

	/**
	 * 解析常用线路管理列表。
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<MDict_Line_Sheet> parseCommonlyLineList(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		List<MDict_Line_Sheet> dataList = JACKSON_MAPPER.readValue(jsonStr,
				new TypeReference<List<MDict_Line_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 解析常用车辆管理列表。
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<MDict_CarCode_Sheet> parseCommonlyCarList(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		List<MDict_CarCode_Sheet> dataList = JACKSON_MAPPER.readValue(jsonStr,
				new TypeReference<List<MDict_CarCode_Sheet>>() {
				});
		return dataList;
	}

	/**
	 * 解析我的评分列表。
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<OrderEvaluationListInfo> parseOrderEvaluationList(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		List<OrderEvaluationListInfo> dataList = JACKSON_MAPPER.readValue(jsonStr,
				new TypeReference<List<OrderEvaluationListInfo>>() {
				});
		return dataList;
	}

	/**
	 * 解析车辆跟踪列表
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<FollowCarListRequestBean> parseFollowList(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		List<FollowCarListRequestBean> dataList = JACKSON_MAPPER.readValue(jsonStr,
				new TypeReference<List<FollowCarListRequestBean>>() {
				});
		return dataList;
	}

	/**
	 * 解析车辆跟踪列表（司机）
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<LocationTrackingBean> parseLocationTrackingList(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		List<LocationTrackingBean> dataList = JACKSON_MAPPER.readValue(jsonStr,
				new TypeReference<List<LocationTrackingBean>>() {
				});
		return dataList;
	}
	
	/**
	 * 解析车辆跟踪列表（司机）
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<AdvertPicBean> parseadvertPicList(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		List<AdvertPicBean> dataList = JACKSON_MAPPER.readValue(jsonStr,
				new TypeReference<List<AdvertPicBean>>() {
				});
		return dataList;
	}

}
