package com.yijian.them.ui.home.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.yijian.them.R;
import com.yijian.them.basic.BasicFragment;
import com.yijian.them.common.App;
import com.yijian.them.ui.home.adapter.AddressAdapter;
import com.yijian.them.utils.location.LocationUtil;
import com.yqjr.utils.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressFragment extends BasicFragment {
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.lvAddress)
    ListView lvAddress;
    private AddressAdapter addressAdapter;
    private double latitude;
    private double longitude;

    @Override
    protected void onClickEvent() {

    }

    @Override
    protected View getResourceView() {
        View rootView = View.inflate(getActivity(), R.layout.fragment_address, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView(Bundle bundle) {
        location();
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem item = addressAdapter.getItem(position);
                String title = item.getTitle();
                String snippet = item.getSnippet();
                String cityCode = item.getCityCode();
                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("cityCode", cityCode);
                intent.putExtra("localName", snippet);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                getActivity().setResult(1, intent);
                getActivity().finish();
            }
        });
    }

    private final int LOCATION_FIND = 10000;

    private void location() {
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                    LOCATION_FIND);
        } else {
            getLocationList();
        }
    }

    private void getLocationList() {
        App.locationUtil.setOnLocationAddressListener(new LocationUtil.OnLocationAddressListener() {

            @Override
            public void onLocation(double latitude, double longitude, String cityCode, String name, String address) {
                AddressFragment.this.latitude = latitude;
                AddressFragment.this.longitude = longitude;
                PoiSearch.Query query = new PoiSearch.Query(name, "", cityCode);
                query.setPageSize(30);// 设置每页最多返回多少条poiitem
                query.setPageNum(1);//设置查询页码
                PoiSearch poiSearch = new PoiSearch(getActivity(), query);
                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                    @Override
                    public void onPoiSearched(PoiResult poiResult, int i) {
                        if (i == 1000) {
                            ArrayList<PoiItem> pois = poiResult.getPois();
                            if (pois != null && pois.size() > 0) {
                                addressAdapter = new AddressAdapter(pois);
                                lvAddress.setAdapter(addressAdapter);
                            }
                        }
                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {

                    }
                });
                poiSearch.searchPOIAsyn();
            }
        });
        App.locationUtil.onceLocation(true);
        App.locationUtil.startLocation();
    }


    /**
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_FIND) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationList();
            } else {
                ToastUtils.toastCenter(getActivity(), "请开启定位权限");
            }
        }
    }


    @OnClick(R.id.tvSearch)
    public void onViewClicked() {
    }
}
