package com.thinkincode.quran.sdk.database;

/**
 * <p>Represents the result of an "EXPLAIN QUERY PLAN" query.</p>
 *
 * <p>TODO: move this out to the Android Utils library.</p>
 */
public class QueryPlan {

    private final int selectId;
    private final int order;
    private final int from;
    private final String detail;

    /**
     * Constructor.
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
