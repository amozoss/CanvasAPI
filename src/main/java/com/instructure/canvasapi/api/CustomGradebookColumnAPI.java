package com.instructure.canvasapi.api;

import com.instructure.canvasapi.model.ColumnDatum;
import com.instructure.canvasapi.model.CustomColumn;
import com.instructure.canvasapi.utilities.APIHelpers;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Copyright (c) 2015 Instructure. All rights reserved.
 */

public class CustomGradebookColumnAPI {

    private static String getGradebookColumnsCacheFilename(long courseId){
        return "api/v1/courses/" + courseId + "/custom_gradebook_columns";
    }

    private static String getColumnDataCacheFilename(long courseId, long columnId){
        return "api/v1/courses/" + courseId + "/custom_gradebook_columns/" + columnId + "/data";
    }

    public interface CustomGradebookInterface {
        @GET("/courses/{course_id}/custom_gradebook_columns")
        void getGradebookColumns(@Path("course_id") long courseId, CanvasCallback<CustomColumn[]> callback);

        @POST("/courses/{course_id}/custom_gradebook_columns")
        void createGradebookColumn(@Path("course_id") long courseId, @Query("column[title]") String title, @Query("column[position]") int position, @Query("column[hidden]") boolean isHidden, @Query("column[teacher_notes]") boolean isTeacherNotes, CanvasCallback<CustomColumn> callback);

        @GET("/courses/{course_id}/custom_gradebook_columns/{column_id}/data")
        void getColumnData(@Path("course_id") long courseId, @Path("column_id") long columnId, CanvasCallback<ColumnDatum[]> callback);

        @PUT("/courses/{course_id}/custom_gradebook_columns/{column_id}/data/{user_id}")
        void updateColumnData(@Path("course_id") long courseId, @Path("column_id") long columnId, @Path("user_id") long user_Id, @Query("column_data[content]") String content, CanvasCallback<ColumnDatum> callback);
    }

    /////////////////////////////////////////////////////////////////////////
    // Build Interface Helpers
    /////////////////////////////////////////////////////////////////////////

    private static CustomGradebookInterface buildInterface(CanvasCallback<?> callback) {
        RestAdapter restAdapter = CanvasRestAdapter.buildAdapter(callback);
        return restAdapter.create(CustomGradebookInterface.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // API Calls
    /////////////////////////////////////////////////////////////////////////

    public static void getGradebookColumns(long courseID, CanvasCallback<CustomColumn[]> callback) {
        if (APIHelpers.paramIsNull(callback)) { return; }

        callback.readFromCache(getGradebookColumnsCacheFilename(courseID));
        buildInterface(callback).getGradebookColumns(courseID, callback);
    }

    public static void createGradebookColumns(long courseId, String title, int position, boolean isHidden, boolean isTeacherNotes, CanvasCallback<CustomColumn> callback) {
        if (APIHelpers.paramIsNull(callback, title)) { return; }

        buildInterface(callback).createGradebookColumn(courseId, title, position, isHidden, isTeacherNotes, callback);
    }

    public static void getColumnData(long courseID, long columnId, CanvasCallback<ColumnDatum[]> callback) {
        if (APIHelpers.paramIsNull(callback)) { return; }

        callback.readFromCache(getColumnDataCacheFilename(courseID, columnId));
        buildInterface(callback).getColumnData(courseID, columnId, callback);
    }

    public static void updateColumnData(long courseID, long columnId, long userId, String content, CanvasCallback<ColumnDatum> callback) {
        if (APIHelpers.paramIsNull(callback)) { return; }

        buildInterface(callback).updateColumnData(courseID, columnId, userId, content, callback);
    }
}
