package com.nbu.controller;


import com.nbu.mapper.EssayMapper;
import com.nbu.pojo.Essay;
import com.nbu.util.JsonUtil;
import com.nbu.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/ccr")
public class CCRController {
    private static Logger logger = LoggerFactory.getLogger(CCRController.class);


    @Autowired
    @Qualifier("EssayMapper")
    private EssayMapper essayMapper;

    @Autowired
    private JsonUtil jsonUtil;

    @PostMapping("/query/{username}")
    @ApiOperation("论文信息普通用户查看")
    public String QueryUserProjectAndEssay(@PathVariable("username") String username){
        return jsonUtil.ObjectToJsonString( new Result("ok",essayMapper.SelectMyProjectAndEasyNom(username)));
    }
    @PostMapping("/query/adm")
    @ApiOperation("管理员查看用户信息")
    public String QueryALLUserProjectAndEssay(){
        return jsonUtil.ObjectToJsonString( new Result("ok",essayMapper.SelectMyProjectAndEasyAdm()));
    }

    @PostMapping("/delete/{username}")
    @ApiOperation("删除论文信息")
    public String DeleteUserProjectAndEssay(@PathVariable("username") String username){
        essayMapper.DeleteInfoByUsername(username);
        return jsonUtil.ObjectToJsonString(new Result("ok"));
    }

    @PostMapping("/update/project/{username}/{my_project}")
    @ApiOperation("修改特定用户的工程")
    public String UpdateUserProject(@PathVariable("username") String username,@PathVariable("my_project")String myProject){
        essayMapper.UpdateInfoByUsernameProject(username,myProject);
        return jsonUtil.ObjectToJsonString(new Result("ok"));
    }

    @PostMapping("/update/essay/{username}/{my_essay}")
    @ApiOperation("修改特定用户的论文信息")
    public String UpdateUserEssay(@PathVariable("username") String username,@PathVariable("my_essay")String myEssay){
        essayMapper.UpdateInfoByUsernameEssay(username,myEssay);
        return jsonUtil.ObjectToJsonString(new Result("ok"));
    }

    @PostMapping("/insert/{username}/{my_project}/{my_essay}")
    @ApiOperation("插入一条信息")
    public String InsertInfo(@PathVariable("username") String username,
                             @PathVariable("my_project") String myProject,
                             @PathVariable("my_essay") String myEssay){
        Essay essay = new Essay();
        if (essayMapper.SelectMyProjectAndEasyAdm() == null){essay.setId(1);}

        essay.setMyProject(myProject);
        essay.setMyEssay(myEssay);
        essay.setOwner(username);
        essay.setScore(-1);
        essayMapper.InsertAInfo(essay);
        return jsonUtil.ObjectToJsonString(new Result("ok"));
    }
    @PostMapping("/insert/{username}/{score}")
    @ApiOperation("打分")
    public String UpdateScore(@PathVariable("username") String username,@PathVariable("score") Integer score) {
        essayMapper.UpdateScoreByUsername(username,score);
        return jsonUtil.ObjectToJsonString(new Result("ok"));
    }
}
