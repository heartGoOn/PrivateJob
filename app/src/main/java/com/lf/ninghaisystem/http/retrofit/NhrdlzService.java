package com.lf.ninghaisystem.http.retrofit;

import com.lf.ninghaisystem.bean.Feedback;
import com.lf.ninghaisystem.bean.HasReport;
import com.lf.ninghaisystem.bean.User;
import com.lf.ninghaisystem.bean.entity.BannerEntity;
import com.lf.ninghaisystem.bean.entity.Duty;
import com.lf.ninghaisystem.bean.entity.DutyPDetail;
import com.lf.ninghaisystem.bean.entity.DutyPerson;
import com.lf.ninghaisystem.bean.entity.DutyRank;
import com.lf.ninghaisystem.bean.entity.DutyType;
import com.lf.ninghaisystem.bean.entity.ImageData;
import com.lf.ninghaisystem.bean.entity.LoginUser;
import com.lf.ninghaisystem.bean.entity.MyProject;
import com.lf.ninghaisystem.bean.entity.PJWord;
import com.lf.ninghaisystem.bean.entity.PjWordType;
import com.lf.ninghaisystem.bean.entity.Project;
import com.lf.ninghaisystem.bean.entity.Result;
import com.lf.ninghaisystem.contact.SortModel;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by admin on 2017/11/22.
 */

public interface NhrdlzService {

    /**
     * 项目列表
     */
    @POST("project/list.do")
    Call<Result<List<Project>>> getProjectList(@Body RequestBody body);


    /***
     * 通过父项目进入列出项目
     */
    @POST("project/listForSecond.do")
    Call<Result<List<Project>>> getChildrentProjectList(@Body RequestBody body);

    /**
     * 项目文档类型列表
     */
    @POST("project/document/type/list.do")
    Call<Result<List<PjWordType>>> getPjWordTypeList(@Body RequestBody body);

    /**
     * 项目文档列表
     */
    @POST("project/document/list.do")
    Call<Result<List<PJWord>>> getPjWordList(@Body RequestBody body);

    /**
     * 履职代表列表
     */
    @POST("project/member/list.do")
    Call<Result<List<DutyPerson>>> getDutyPersonList(@Body RequestBody body);

    /**
     * 履职代表详情
     */
    @POST("project/member/get.do")
    Call<Result<DutyPDetail>> getDutyPDetail(@Body RequestBody body);

    /**
     * 履职记录列表
     */
    @POST("project/duty/perform/list.do")
    Call<Result<List<Duty>>> getDutyList(@Body RequestBody body);

    /**
     * 活跃度排行列表
     */
    @POST("project/duty/perform/analysis/liveness/list.do")
    Call<Result<List<DutyRank>>> getDutyRankList(@Body RequestBody body);

    /**
     * 图片上传 --多表单形式
     */
    @POST("project/duty/perform/upload.do")
    Call<Result<List<ImageData>>> postImageList(@Body RequestBody body);

    /**
     * 获取自己的履职项目
     */
    @POST("project/member/listMyProject.do")
    Call<Result<List<MyProject>>> getMyProjectList(@Body RequestBody body);

    /**
     * 获取履职类型
     */
    @POST("project/duty/perform/type/list.do")
    Call<Result<List<DutyType>>> getDutyType(@Body RequestBody body);

    /**
     * 插入履职记录接口
     */
    @POST("project/duty/perform/insert.do")
    Call<Result> insertDutyPerform(@Body RequestBody body);

    /**
     * 增加进度报告接口
     */
    @POST("project/report/insertNewReport.do")
    Call<Result<HasReport>> insertReportProcess(@Body RequestBody body);

    /**
     * 更换密码
     */
    @POST("employee/resetPassword.do")
    Call<Result> resetPassword(@Body RequestBody body);

    /**
     * 通讯录
     */
    @POST("employee/AddressList.do")
    Call<Result<List<SortModel>>> getAddressList(@Body RequestBody body);

    /**
     * 登录
     */
    @POST("employee/login.do")
    Call<Result<LoginUser>> getLoginMsg(@Body RequestBody body);

    /**
     * 个人信息
     */
    @POST("employee/MyDetails.do")
    Call<Result<User>> getMineMsg(@Body RequestBody body);

    /**
     * 新闻轮播
     */
    @POST("news/NewsList.do")
    Call<Result<List<BannerEntity>>> getBannerList(@Body RequestBody body);

    /**
     * 意见反馈
     */
    @POST("employee/suggestion/insert.do")
    Call<Result> insertSugFeedback(@Body RequestBody body);

    /**
     * 获取我的履职列表
     */
    @POST("project/duty/perform/my/list.do")
    Call<Result<List<Duty>>> getMyDutyList(@Body RequestBody body);

    /**
     * 获取履职反馈个数
     */
    @POST("project/duty/perform/feedback/count.do")
    Call<Result<Feedback>> getFeedbackCount(@Body RequestBody body);

    /**
     * 获取项目相关履职人
     */
    @POST("project/member/single/list.do")
    Call<Result<List<DutyPerson>>> getDutyPersonSelect(@Body RequestBody body);

    /**
     * 助推提交
     */
    @POST("project/duty/perform/notice/insert.do")
    Call<Result> insertBoost(@Body RequestBody body);

    /**
     * 插入履职记录回复
     */
    @POST("project/duty/perform/evaluating/insert.do")
    Call<Result> insertEvaluating(@Body RequestBody body);

    /**
     * 更新cid接口
     */
    @POST("employee/cid/update.do")
    Call<Result> updateCid(@Body RequestBody body);

    /**
     * 更新履职反馈消息阅读状态
     */
    @POST("project/duty/perform/evaluating/update.do")
    Call<Result> updateFeedbackRead(@Body RequestBody body);

    /**
     * 更新履职助推消息阅读状态
     */
    @POST("project/duty/perform/notice/update.do")
    Call<Result> updateBoostRead(@Body RequestBody body);

    /**
     * 更新进度报告
     */
    @POST("project/report/update.do")
    Call<Result> updateProcessReport(@Body RequestBody body);

    /**
     * 获取部门评价是否开发接口
     */
    @POST("project/department/evaluating/open/check.do")
    Call<Result<Boolean>> checkDepartmentIsOpen(@Body RequestBody body);

    /**
     * 获取代表评价是否开放接口
     */
    @POST("project/member/evaluating/open/check.do")
    Call<Result<Boolean>> checkEvaluatingIsOpen(@Body RequestBody body);

    /**
     * 获取项目评价是否开放接口
     */
    @POST("pmProjectSatisDegree/open/check.do")
    Call<Result<Boolean>> checkProjectIsOpen(@Body RequestBody body);

    /**
     * 判断有无进度报告接口
     */
    @POST("project/report/judgeReport.do")
    Call<Result<HasReport>> checkHasReport(@Body RequestBody body);

}
