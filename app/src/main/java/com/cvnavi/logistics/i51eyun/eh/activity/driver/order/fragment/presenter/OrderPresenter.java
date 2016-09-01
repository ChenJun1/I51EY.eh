package com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.presenter;

import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.model.IOrderBiz;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.model.OrderBiz;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.model.OrderListener;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.view.OrderView;
import com.cvnavi.logistics.i51eyun.eh.utils.JsonUtils;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;
import java.util.List;

public class OrderPresenter {

	private OrderView orderView;
	private IOrderBiz orderBiz;

	public OrderPresenter(OrderView mOrderView) {
		this.orderView = mOrderView;
		this.orderBiz = new OrderBiz();
	}

	public void getCompletedLoading() {
		orderView.showLoading();
		orderBiz.completedLoading(orderView.getPageStartNum(false), orderView.getPageEndNum(false),
				orderView.getOperateCode(), orderView.getTime(false), new OrderListener() {
					@Override
					public void OrderError(String error) {
						orderView.hideLoading();
						orderView.toError(error);
					}

					@Override
					public void OrderSuccess(String str) {
						orderView.hideLoading();
						try {
							List<WaitingOrderBaseInfo> driverData = JsonUtils.parseOrderBeanList(str);
							orderView.toCompletedLoading(driverData);
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public void getCompletedRefresh() {
		orderView.showLoading();
		orderBiz.completedRefresh(orderView.getPageStartNum(true), orderView.getPageEndNum(true),
				orderView.getOperateCode(), orderView.getTime(true), new OrderListener() {
					@Override
					public void OrderError(String error) {
						orderView.hideLoading();
						orderView.toError(error);
					}

					@Override
					public void OrderSuccess(String str) {
						orderView.hideLoading();
						try {
							List<WaitingOrderBaseInfo> driverData = JsonUtils.parseOrderBeanList(str);
							orderView.toCompletedRefresh(driverData);
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public void getReceivedLoading() {
		orderView.showLoading();
		orderBiz.receivedLoading(orderView.getPageStartNum(false), orderView.getPageEndNum(false),
				orderView.getOperateCode(), orderView.getTime(false), new OrderListener() {
					@Override
					public void OrderError(String error) {
						orderView.hideLoading();
						orderView.toError(error);
					}

					@Override
					public void OrderSuccess(String str) {
						orderView.hideLoading();
						try {
							List<WaitingOrderBaseInfo> driverData = JsonUtils.parseOrderBeanList(str);
							orderView.toReceivedLoading(driverData);
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public void getReceivedRefresh() {
		orderView.showLoading();
		orderBiz.receivedRefresh(orderView.getPageStartNum(true), orderView.getPageEndNum(true),
				orderView.getOperateCode(), orderView.getTime(true), new OrderListener() {
					@Override
					public void OrderError(String error) {
						orderView.hideLoading();
						orderView.toError(error);
					}

					@Override
					public void OrderSuccess(String str) {
						orderView.hideLoading();
						try {
							List<WaitingOrderBaseInfo> driverData = JsonUtils.parseOrderBeanList(str);
							orderView.toReceivedRefresh(driverData);
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public void getWaitingRefresh() {
		orderView.showLoading();
		orderBiz.waitingRefresh(orderView.getPageStartNum(true), orderView.getPageEndNum(true),
				orderView.getOperateCode(), orderView.getTime(true), new OrderListener() {
					@Override
					public void OrderError(String error) {
						orderView.hideLoading();
						orderView.toError(error);
					}

					@Override
					public void OrderSuccess(String str) {
						orderView.hideLoading();
						try {
							List<WaitingOrderBaseInfo> driverData = JsonUtils.parseOrderBeanList(str);
							orderView.toWaitingRefresh(driverData);
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public void getWaitingLoading() {
		orderView.showLoading();
		orderBiz.waitingLoading(orderView.getPageStartNum(false), orderView.getPageEndNum(false),
				orderView.getOperateCode(), orderView.getTime(false), new OrderListener() {
					@Override
					public void OrderError(String error) {
						orderView.hideLoading();
						orderView.toError(error);
					}

					@Override
					public void OrderSuccess(String str) {
						orderView.hideLoading();
						try {
							List<WaitingOrderBaseInfo> driverData = JsonUtils.parseOrderBeanList(str);
							orderView.toWaitingLoading(driverData);
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public void deleteOrder(String orderKey) {
		orderView.showLoading();
		orderBiz.completedOrder(orderKey, new OrderListener() {
			@Override
			public void OrderSuccess(String str) {
				orderView.hideLoading();
				orderView.toDelete();
			}

			@Override
			public void OrderError(String str) {
				orderView.hideLoading();
				orderView.toError(str);
			}
		});
	}

	public void completedOrder(String orderKey) {
		orderView.showLoading();
		orderBiz.completedOrder(orderKey, new OrderListener() {
			@Override
			public void OrderSuccess(String str) {
				orderView.hideLoading();
				orderView.toDelete();
			}

			@Override
			public void OrderError(String str) {
				orderView.hideLoading();
				orderView.toError(str);
			}
		});
	}
}
