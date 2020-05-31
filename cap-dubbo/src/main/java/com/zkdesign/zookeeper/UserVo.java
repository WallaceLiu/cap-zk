package com.zkdesign.zookeeper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qiurunze
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements Serializable {

    Integer id;
    String name;
    Date birthDay;
    int port;

    @Override
    public String toString() {
        return "UserVo{" + "id=" + id + ", name='" + name + '\'' +
                ", birthDay=" + birthDay + ", port=" + port + '}';
    }
}
