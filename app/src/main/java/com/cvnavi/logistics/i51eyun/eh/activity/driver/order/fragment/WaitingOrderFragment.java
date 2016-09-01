package com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.eh.BaseFragment;
import com.cvnavi.logistics.i51eyun.eh.Constants;
import com.cvnavi.logistics.i51eyun.eh.R;
import com.cvnavi.logistics.i51eyun.eh.activity.bean.order.WaitingOrderBaseInfo;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.D_WaitingOrderListAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.D_WaitingOrderDetailActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.presenter.OrderPresenter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.view.OrderView;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.listview.MyListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangshy on 2016/4/22. 未接订单
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.d_waitingfragment)
public class WaitingOrderFragment extends BaseFragment implements
		PullToRefreshLayout.OnRefreshListener, OrderView {

	public static WaitingOrderFragment getFragment() {
		return new WaitingOrderFragment();
	}

	@ViewInject(R.id.c_refresh_received_view)
	private PullToRefreshLayout c_refresh_received_view;
	@ViewInject(R.id.c_received_order_lv)
	private MyListView c_received_order_lv;
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	private D_WaitingOrderListAdapter waitingOrderAdapter;
	private List<WaitingOrderBaseInfo> waitingOrderList;
	// private EmptyLayout emptyLayout;

	private CommonWaitDialog waitDialog;
	private Context context;
	private OrderPresenter orderPresenter;

	private int startNum = Constants.FindStartNum;
	private int findNum = Constants.FindNum;
	private String requestTime = DateUtil.getCurDateStr();

	private Handler myFreshHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		waitingOrderList = new ArrayList<>();
		orderPresenter = new OrderPresenter(WaitingOrderFragment.this);

		waitDialog = new CommonWaitDialog(context, R.style.progress_dialog);
		waitingOrderAdapter = new D_WaitingOrderListAdapter(context,
				waitingOrderList);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		c_refresh_received_view.setOnRefreshListener(this);
		c_received_order_lv.setAdapter(waitingOrderAdapter);
		c_received_order_lv
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						WaitingOrderBaseInfo bean = waitingOrderList
								.get(position);
						if (bean != null) {
							Intent intent = new Intent(context,
									D_WaitingOrderDetailActivity.class);
							intent.putExtra(Constants.OrderInfo, bean);
							startActivity(intent);
						}
					}
				});
		c_received_order_lv.setEmptyView(empty_list_view);

		// emptyLayout = new EmptyLayout(context);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!NetWorkUtils.isNetWort(getActivity())){
			return;
		}
		orderPresenter.getWaitingRefresh();
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

		myFreshHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		if(!NetWorkUtils.isNetWort(getActivity())){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		orderPresenter.getWaitingRefresh();
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {

		myFreshHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				waitingOrderAdapter.notifyDataSetChanged();
			}
		};
		if(!NetWorkUtils.isNetWort(getActivity())){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		orderPresenter.getWaitingLoading();
	}

	@Override
	public void showLoading() {
		if (this.getView().getVisibility() == View.VISIBLE) {
			waitDialog.show();
		}
	}

	@Override
	public void hideLoading() {
		waitDialog.dismiss();
		if (myFreshHandler != null) {
			myFreshHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void toError(String error) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("提示");
		alertDialog.setMessage(error);
		alertDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
		alertDialog.create().show();
	}

	@Override
	public void toWaitingRefresh(List<WaitingOrderBaseInfo> dataList) {
		if (dataList != null) {
			waitingOrderList.clear();
			waitingOrderList.addAll(dataList);

			waitingOrderAdapter.notifyDataSetChanged();
		}
		// else{
		// emptyLayout.showEmpty();
		// }
	}

	@Override
	public void toWaitingLoading(List<WaitingOrderBaseInfo> dataList) {
		if (dataList != null && dataList.size() > 0) {
			waitingOrderList.addAll(dataList);

			waitingOrderAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(getActivity(), Constants.NoDataMsg, Toast.LENGTH_SHORT).show();
			// emptyLayout.showEmpty();
		}
	}

	@Override
	public void toDelete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("提示");
		builder.setMessage("运单删除成功！");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		builder.create().show();
	}

	@Override
	public int getPageStartNum(boolean isRefresh) {
		if (isRefresh) {
			startNum = Constants.FindStartNum;
			return startNum;
		} else {
			startNum = waitingOrderList.size() + startNum;
		}
		return startNum;
	}

	@Override
	public int getPageEndNum(boolean isRefresh) {
		if (isRefresh) {
			findNum = Constants.FindNum;
			return findNum;
		} else {
			findNum = waitingOrderList.size() + Constants.FindNum;
		}
		return findNum;
	}

	@Override
	public String getOperateCode() {
		return "AA";
	}

	@Override
	public String getTime(boolean isRefresh) {
		if (isRefresh) {
			requestTime = DateUtil.getCurDateStr();
			return requestTime;
		}
		return requestTime;
	}

	@Override
	public void toCompletedLoading(List<WaitingOrderBaseInfo> OrderBaseList) {
	}

	@Override
	public void toCompletedRefresh(List<WaitingOrderBaseInfo> OrderBaseList) {
	}

	@Override
	public void toReceivedLoading(List<WaitingOrderBaseInfo> OrderBaseList) {
	}

	@Override
	public void toReceivedRefresh(List<WaitingOrderBaseInfo> OrderBaseList) {
	}
}
