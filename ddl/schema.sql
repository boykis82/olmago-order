docker run -it mysql:5.7 mysql

alter database olmago_order default character set utf8;

drop table if exists message_in_box CASCADE ;
drop table if exists msg_envelope CASCADE ;
drop table if exists ord CASCADE ;
drop table if exists ord_dtl CASCADE;

create table message_in_box (
   id varchar(255) not null,
    event_type varchar(255) not null,
    payload varchar(255) not null,
    received_date_time datetime(6) not null,
    primary key (id)
) engine=InnoDB;


create table msg_envelope (
   id bigint not null auto_increment,
   uuid varchar(255) not null,
   msg_typ varchar(10) not null,
    agg_typ varchar(255),
    agg_id varchar(255),
    bind_nm varchar(255) not null,
    created_at datetime(6) not null,
    published_at datetime(6),
    published boolean not null,
    msg_name varchar(255) not null,
    payload varchar(2048) not null,
    primary key (id)
) engine=InnoDB;


create table ord (
   id bigint not null auto_increment,
    cncl_req_dtm datetime(6),
    cncl_dtm datetime(6),
    cmpl_dtm datetime(6),
    cust_id bigint not null,
    ord_typ varchar(30) not null,
    pay_id bigint,
    rcv_cmpl_dtm datetime(6),
    rcv_req_dtm datetime(6) not null,
    version integer not null,
    primary key (id)
) engine=InnoDB;

create table ord_dtl (
   id bigint not null auto_increment,
    cncl_req_dtm datetime(6),
    cncl_dtm datetime(6),
    cmpl_dtm datetime(6),
    contract_id bigint,
    contract_type varchar(10) not null,
    product_code varchar(10) not null,
    rcv_cmpl_dtm datetime(6),
    rcv_req_dtm datetime(6) not null,
    version integer not null,
    order_id bigint,
    primary key (id)
) engine=InnoDB;

create index message_envelope_n1 on msg_envelope (published, id);
create index ord_n1 on ord (cust_id, id desc);
create index ord_dtl_n1 on ord_dtl (order_id, id);

alter table ord_dtl
   add constraint ord_dtl_fk_ord_id
   foreign key (order_id)
   references ord;