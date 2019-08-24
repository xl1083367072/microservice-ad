package com.xl.ad.sender;

import com.xl.ad.mysql.dto.MySqlRowData;

public interface Sender {

    void sender(MySqlRowData rowData);

}
