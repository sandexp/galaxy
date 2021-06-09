package com.github.galaxy.service;

/**
 * 实时血缘关系分析
 * 实时数据join通过Kafka的KSQL与维表(来自KV/RDBMS)进行连接查询,将其送入到下一层级的topic中
 * 实时血缘分析解决的石topic之间字段连接关系
 */
public class OnlineLineageAnalysis extends LineageAnalysis{

}
