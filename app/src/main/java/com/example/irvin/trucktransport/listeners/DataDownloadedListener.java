package com.example.irvin.trucktransport.listeners;

import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.model.ResultBundle;

/**
 * Created by IvanX on 16.4.2017..
 */

public interface DataDownloadedListener  {

    /**
     * Callback to handle ui after data has been downloaded
     *
     * @param queryType
     *            to distinguish for which API call , this callback is received
     * @param object
     *            returns response data
     */
    public void dataDownloaded(QueryType queryType, ResultBundle object);

    /**
     * Callback when error occurs during asyncdownloading of data or during
     * parsing
     *
     * @param queryType
     *            to distinguish for which API call , this callback is received
     * @param object
     *            returns response data
     */
    public void onErrorLoading(QueryType queryType, ResultBundle object);
}
