package com.reader.modulereader.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：zhaokang on 2017/12/14 14:19
 * 邮箱：zhaokang@diandian-tech.com
 * 描述：订单详情
 */

public class OrderDetails {

    public String back_memo;
    public String pay_code;
    public String pay_type_id;
    public int back_amount;
    public String original_price;//原总价
    public String coupontype;//优化劵类型，
    public int is_open_swiftnum;//流水号是否可见,1可见，0不可见
    public String send_addr;//桌台号
    public String new_coupon_id;//新优惠劵，
    public String memo;//整单备注
    public int dinein_flag;//堂食标签
    public String ret_msg;
    public double all_discount_amount;//总优惠
    public int pay_status;//支付状态，1已经支付，-1未支付 ,0-等待支付
    public Object new_coupon_status;
    public Object gb_code;
    public int id;
    public String pay_status_desc;
    public double pay_price;//支付金额，未支付也有值
    public String address;//商家地址
    public double total_price;//总价
    public String status_desc;//订单状态值
    public String swift_number;//流水号，与is_open_swiftnum字段对应
    public String food_code;//取餐码
    public String mobile;
    public String ordertime;
    public Object share_coupon_url;
    public int shop_id;
    public String predict_cooked_time;
    public String table_code;
    public String recvtime;
    public int user_id;
    public String shopname;//商家名称
    public int is_share_coupon;
    public int gbp_id;
    public String subsidy_amt;
    public String pic_url;//商家logo
    public int ret_code;
    public int status;//订单状态，0初始化，29商家已接单，99订单结束，30接单超时，-1退单
    public String back_swift_num; //退菜退单流水号 退单退菜情况

    public List<ProductsBean> products;//多个菜品

    public static String getPayStatus(int pay_status){
        String resp = "";
        switch (pay_status) {
            case -1:
                resp = "未支付";
                break;
            case 0:
                resp = "等待支付";
                break;
            case 1:
                resp = "已经支付";
                break;
        }
        return resp;
    }

    public static class ProductsBean {

        public double original_price;//菜品原价
        public int box_number;
        public int quantity;//数量
        public String itemname;//菜品名称
        public int number_order;
        public int weight;
        public double box_price;
        public String big_pic;
        public int per_head;
        public String pro_unit_type;
        public int package_number;
        public String pro_unit;
        public double price;
        public String small_pic;
        public String package_name;
        public int id;//菜品ID
        public int is_printed;
        public int is_attach;
        public String remarks;
        public int status;//订单菜品状态，0无效，1有效，2退菜[增加退菜功能，订单详情中展示退菜情况及退菜数量

        public List<SuitProductBean> suit_products;//对应套餐，见菜品中套餐

        public static class SuitProductBean implements Serializable {

            public String sub_prod_name;
            public int sub_prod_id;
            public int suit_group_id;
            public String suit_group_name;
            public double inc_price;
        }
    }
}
