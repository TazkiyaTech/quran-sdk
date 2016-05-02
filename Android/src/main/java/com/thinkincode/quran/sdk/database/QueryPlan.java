package com.thinkincode.quran.sdk.database;

/**
 * Represents the result of an "EXPLAIN QUERY PLAN" query.
 */
public class QueryPlan {

    private final int selectId;
    private final int order;
    private final int from;
    private final String detail;

    /**
     * Constructor.
     *
     * @param selectId
     * @param order
     * @param from
     * @param detail
     */
    public QueryPlan(int selectId, int order, int from, String detail) {
        this.selectId = selectId;
        this.order = order;
        this.from = from;
        this.detail = detail;
    }

    public int getSelectId() {
        return selectId;
    }

    public int getOrder() {
        return order;
    }

    public int getFrom() {
        return from;
    }

    public String getDetail() {
        return detail;
    }
}
