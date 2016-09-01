/**
 * Administrator2016-5-23
 */
package com.cvnavi.logistics.i51eyun.eh.activity.bean.car;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.paging.PagingBean;

/**
 * 版权所有 上海势航
 *
 * @author chenJun and johnnyYuan
 * @description
 * @date 2016-5-23 下午1:27:25
 * @version 1.0.0
 * @email yuanlunjie@163.com
 */

public class RequestCarList extends PagingBean {

	  /// <summary>
    /// 始发省
    /// </summary>
    public String Provenance_Provice;
    /// <summary>
    /// 始发市
    /// </summary>
    public String Provenance_City;
    /// <summary>
    /// 目的省
    /// </summary>
    public String Destination_Provice;
    /// <summary>
    /// 目的市
    /// </summary>
    public String Destination_City;
    /// <summary>
    /// 车型
    /// </summary>
    public String CarType;
    
    public String CarType_Oid;
    /// <summary>
    /// 车牌
    /// </summary>
    public String CarCode;
    /// <summary>
    /// 尺寸
    /// </summary>
    public String Size;

}
