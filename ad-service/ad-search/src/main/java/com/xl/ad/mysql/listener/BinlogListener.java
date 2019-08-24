package com.xl.ad.mysql.listener;

import com.xl.ad.mysql.dto.BinlogRowData;

public interface BinlogListener {

    void register();

    void onEvent(BinlogRowData rowData);

}
