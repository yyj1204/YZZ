package com.example.cm.juyiz.adapter.impl;

import com.example.cm.juyiz.bean.Address;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public interface AddressManagerView {

    public void setDefaultListener(Address address, int position);

    public void setEditListener(Address address);

    public void setDeleteListener(Address address, int position);


}
