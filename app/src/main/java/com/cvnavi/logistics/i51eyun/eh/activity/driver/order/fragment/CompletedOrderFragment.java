package com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.cvnavi.logistics.i51eyun.eh.activity.driver.adapter.D_CompletedOrderListAdapter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.D_CompletedOrderDetailActivity;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.presenter.OrderPresenter;
import com.cvnavi.logistics.i51eyun.eh.activity.driver.order.fragment.view.OrderView;
import com.cvnavi.logistics.i51eyun.eh.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.eh.utils.NetWorkUtils;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout;
import com.cvnavi.logistics.i51eyun.eh.widget.PullToRefreshLayout.OnRefreshListener;
import com.cvnavi.logistics.i51eyun.eh.widget.dialog.CommonWaitDialog;
import com.cvnavi.logistics.i51eyun.eh.widget.listview.MyListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangshy on 2016/4/22. 完成订单
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.d_cmpletefragment)
public class CompletedOrderFragment extends BaseFragment implements OnRefreshListener, OrderView {

	public static CompletedOrderFragment getFragment() {
		return new CompletedOrderFragment();
	}

	@ViewInject(R.id.c_refresh_received_view)
	private PullToRefreshLayout c_refresh_received_view;
	@ViewInject(R.id.c_received_order_lv)
	private MyListView c_received_order_lv;
	@ViewInject(R.id.empty_list_view)
	private TextView empty_list_view;

	private D_CompletedOrderListAdapter completedAdapter;
	private List<WaitingOrderBaseInfo> completedOrderList;

	private OrderPresenter orderPresenter;
	private CommonWaitDialog waitDialog;

	private int startNum = Constants.FindStartNum;
	private int findNum = Constants.FindNum;
	private String requestTime = DateUtil.getCurDateStr();
	
	private Handler myFreshHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		waitDialog = new CommonWaitDialog(getActivity(), R.style.progress_dialog);
		orderPresenter = new OrderPresenter(CompletedOrderFragment.this);
		completedOrderList = new ArrayList<WaitingOrderBaseInfo>();

		completedAdapter = new D_CompletedOrderListAdapter(getActivity(), completedOrderList);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		c_refresh_received_view.setOnRefreshListener(this);
		c_received_order_lv.setAdapter(completedAdapter);
		c_received_order_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				WaitingOrderBaseInfo bean = completedOrderList.get(position);
				Intent intent = new Intent(getActivity(), D_CompletedOrderDetailActivity.class);
				intent.putExtra(Constants.OrderInfo, bean);
				startActivity(intent);
			}
		});
		c_received_order_lv.setEmptyView(empty_list_view);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (this.getView().getVisibility() == View.VISIBLE) {
			orderPresenter.getCompletedRefresh();
		}
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		
		if(!NetWorkUtils.isNetWort(getActivity())){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		myFreshHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		
		orderPresenter.getCompletedLoading();
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		myFreshHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		};
		if(!NetWorkUtils.isNetWort(getActivity())){
			myFreshHandler.sendEmptyMessage(0);
			return;
		}
		orderPresenter.getCompletedRefresh();
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
		completedAdapter.notifyDataSetChanged();
		if(myFreshHandler!=null){
			myFreshHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void toError(String error) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("提示");
		alertDialog.setMessage(error);
		alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		alertDialog.create().show();
	}

	@Override
	public void toWaitingRefresh(List<WaitingOrderBaseInfo> dataList) {
	}

	@Override
	public void toWaitingLoading(List<WaitingOrderBaseInfo> dataList) {
	}

	@Override
	public void toCompletedLoading(List<WaitingOrderBaseInfo> dataList) {
		if (dataList != null && dataList.size() > 0) {
			completedOrderList.addAll(dataList);

			completedAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(getActivity(), Constants.NoDataMsg, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void toCompletedRefresh(List<WaitingOrderBaseInfo> dataList) {
		if (dataList != null) {
			completedOrderList.clear();
			completedOrderList.addAll(dataList);

			completedAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void toReceivedLoading(List<WaitingOrderBaseInfo> OrderBaseList) {
	}

	@Override
	public void toReceivedRefresh(List<WaitingOrderBaseInfo> OrderBaseList) {
	}

	@Override
	public void toDelete() {
	}

	@Override
	public int getPageStartNum(boolean isRefresh) {
		if (isRefresh) {
			startNum = Constants.FindStartNum;
			return startNum;
		} else {
			startNum = completedOrderList.size() + startNum;
		}
		return startNum;
	}

	@Override
	public int getPageEndNum(boolean isRefresh) {
		if (isRefresh) {
			findNum = Constants.FindNum;
			return findNum;
		} else {
			findNum = completedOrderList.size() + Constants.FindNum;
		}
		return findNum;
	}

	@Override
	public String getOperateCode() {
		return "AG";
	}

	@Override
	public String getTime(boolean isRefresh) {
		if (isRefresh) {
			requestTime = DateUtil.getCurDateStr();
			return requestTime;
		}
		return requestTime;
	}
}
