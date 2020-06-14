package com.hlsii.controller;//package com.hlsii.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.cosylab.hadarshbaseplugin.commdef.PostProcessing;
//import com.hlsii.commdef.PVDataFormat;
//import com.hlsii.commdef.RetrieveParms;
//import com.hlsii.service.RetrieveServiceMultipleThreads;
//import com.hlsii.util.TimeUtil;
//import com.hlsii.vo.RetrieveData;
//import com.hlsii.vo.ReturnWrap;
//import org.springframework.util.StringUtils;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author ScXin
// * @date 5/7/2020 9:14 PM
// */
//public class Main {
//
//    public static void main(String[] args) {
//
//
//        String pvListStr = "RNG:INJ:BPM:BPM1:X";
//        String type = "firstSample";
//        String from = "2020-05-05T13:23:29.000Z";
//        String to = "2020-05-06T13:23:29.000Z";
//
////        HdarsApiController hdarsApiController=new HdarsApiController();
//
//        RetrieveServiceMultipleThreads retrieveService=new RetrieveServiceMultipleThreads();
//        int sampleDuration = 0;
//        List<RetrieveData> result = new ArrayList<>();
//        try {
//            PostProcessing postProcessing = PostProcessing.FIRSTSAMPLE;
//            if (!StringUtils.isEmpty(type)) {
//                postProcessing = PostProcessing.parse(type);
//            }
//            if (postProcessing == PostProcessing.LASTSAMPLE || postProcessing == PostProcessing.NONE) {
//                postProcessing = PostProcessing.FIRSTSAMPLE;
//            }
//
//
//
//            //将前端传过来的pv列表按照","进行分割，得到PV列表
//            List<String> pvList1 = Arrays.asList(pvListStr, ",");
//            Timestamp startTime = TimeUtil.convertFromISO8601String(from);
//            Timestamp endTime = TimeUtil.convertFromISO8601String(to);
//            System.out.println("postProcessing=="+postProcessing.toString());
//            System.out.println("startTime=="+startTime);
//            System.out.println("endTime=="+endTime);
//            if (postProcessing != PostProcessing.NONE) {
//                sampleDuration = retrieveService.getSamplingInterval(pvList1.get(0), postProcessing, startTime, endTime);
//            }
//            if (sampleDuration == 0) {
//                postProcessing = PostProcessing.NONE;
//            }
//
//            System.out.println("sampleDuration=="+sampleDuration);
//
//            RetrieveParms retrieveParms = new RetrieveParms(pvList1, postProcessing, sampleDuration, startTime, endTime, true, PVDataFormat.QW);
//            result = retrieveService.retrievePVData(retrieveParms);
//        } catch (Exception ex) {
//            String msg = "Exception at retrieving data: " + ex.toString();
//            System.out.println("come here");
//        }
//        ReturnWrap rc = new ReturnWrap();
//        JSONObject data = new JSONObject();
//        data.put("list", result);
//        data.put("sampleDuration", sampleDuration);
//        rc.setData(data);
//        System.out.println(rc.getMsg());
//
//
//
////        ReturnWrap returnWrap=hdarsApiController.getHistoryById(pvListStr, type, from, to);
////        System.out.println(returnWrap.getMsg());
//    }
//}
