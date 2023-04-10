package com.tn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tn.common.R;
import com.tn.domain.User;
import com.tn.service.UserService;
import com.tn.utils.SMSUtils;
import com.tn.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.interfaces.PBEKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    SMSUtils smsUtils;

    /**
     * 通过邮箱发送验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            smsUtils.sendSimpleMail(phone,"验证码","您的验证码是"+code);

            session.setAttribute(phone,code);
            R.success("邮箱验证码发送成功");
        }
        return R.error("邮箱验证码发送失败");
    }

    /**
     * 移动端登录功能
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        log.info(map.toString());
        //获取邮箱号
        String phone = map.get("phone").toString();

        /*
        方便测试使用
        如果邮箱号已经保存在user表中,code随便输入,直接登录成功
         */
//        LambdaQueryWrapper<User> testquerywrapper=new LambdaQueryWrapper<>();
//        testquerywrapper.eq(phone != null,User::getPhone,phone);
//        User testuser = userService.getOne(testquerywrapper);
//        if(testuser != null){
//            session.setAttribute("user",testuser.getId());
//            return R.success(testuser);
//        }


        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession != null && codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return  R.error("登录失败");
    }


    @PostMapping("/loginout")
    public R<String> loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }

}
