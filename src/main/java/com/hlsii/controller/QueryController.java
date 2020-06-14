package com.hlsii.controller;

import com.hlsii.util.PVDataTreeUtil;
import com.hlsii.vo.PVDataTree;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.hlsii.util.WebUtil;

//import com.hlsii.service.QueryProfileService;

/**
 * @author ScXin
 * @date 4/26/2020 1:27 PM
 */
@RestController
@RequestMapping("/hdars")
public class QueryController {

//    @Autowired
//    private QueryProfileService queryProfileService;
//
//    @Autowired
//    private WebUtil webUtil;

//    @Autowired
//    private IRecordUserLogService recordUserLogService;

    @ApiOperation("获取PV分组数")
    @GetMapping("/query/getPVGroupTree")
    public PVDataTree predefineGroup() {
        return PVDataTreeUtil.getPVDataTree();
    }

    @ApiOperation("运行状态接口")
    @RequestMapping("/query/operationStatus")
    public void operationStatus() {
//        recordUserLogService.logOperation(OperationType.QUERY_OPERATION_STATUS, null);
    }
//
//
//    @ApiOperation("获取当前用户的profile")
//    @GetMapping("/query/getUserProfile")
//    public List<QueryProfile> profileQuery() {
//        User user = webUtil.getCurrentLoginUser();
////        return queryProfileService.getUserProfiles(user.getId());
//    }
//
//    @ApiOperation("根据profile得到其所有的group")
//    @GetMapping("/query/getProfileGroup/{profId}")
//    public ReturnWrap getProfileGroup(@PathVariable("profId") String profId) {
//        QueryProfile profile = queryProfileService.get(profId);
//        if (profile == null) {
//            return new ReturnWrap(false, "Query profile not found!");
//        }
//        if (profile.getUser().getId().equals(webUtil.getCurrentLoginUser().getId())) {
//            return new ReturnWrap(true, GroupVO.fromGroups(queryProfileService.getProfileGroups(profId)));
//        }
//        return new ReturnWrap(false, "The query profile is not owned by you!");
//    }

//    @RequestMapping("/saveProfile/{profId}/{profName}/{groupParam}")
//    public ReturnWrap saveProfileGroups(@PathVariable("profId") String profId,
//                                        @PathVariable("profName") String profName,
//                                        @PathVariable("groupParam")String groupParam)
//    {
//        QueryProfile profile=new QueryProfile(profId,WebUtil.getCurrentLoginUser(),profName);
//        String[]groupStr=groupParam.split(",");
//        List<ProfileGroup> groups=new ArrayList<>();
//        if(groupStr!=null){
//            for(int i=0;i<groupStr.length;i++){
//                String str=groupStr[i].trim();
//                if(StringUtils.isEmpty(str)){
//                    continue;
//                }
//                String[]s=
//            }
//        }
//    }




}
