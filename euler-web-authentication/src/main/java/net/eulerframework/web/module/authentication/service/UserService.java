package net.eulerframework.web.module.authentication.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.eulerframework.common.util.Assert;
import net.eulerframework.common.util.BeanTool;
import net.eulerframework.web.config.WebConfig;
import net.eulerframework.web.core.base.request.PageQueryRequest;
import net.eulerframework.web.core.base.response.PageResponse;
import net.eulerframework.web.core.base.service.impl.BaseService;
import net.eulerframework.web.module.authentication.dao.IGroupDao;
import net.eulerframework.web.module.authentication.dao.IUserDao;
import net.eulerframework.web.module.authentication.entity.User;
import net.eulerframework.web.module.authentication.exception.IncorrectPasswordException;
import net.eulerframework.web.module.authentication.exception.IncorrectPasswordFormatException;
import net.eulerframework.web.module.authentication.exception.IncorrectPasswordLengthException;
import net.eulerframework.web.module.authentication.exception.IncorrectUserEmailFormatException;
import net.eulerframework.web.module.authentication.exception.IncorrectUsernameFormatException;
import net.eulerframework.web.module.authentication.exception.NullPasswordException;
import net.eulerframework.web.module.authentication.exception.NullUserEmailException;
import net.eulerframework.web.module.authentication.exception.NullUserNameException;
import net.eulerframework.web.module.authentication.exception.UserEmailAlreadyUsedFormatException;
import net.eulerframework.web.module.authentication.exception.UserMobileAlreadyUsedFormatException;
import net.eulerframework.web.module.authentication.exception.UserNotFoundException;
import net.eulerframework.web.module.authentication.exception.UsernameAlreadyUsedFormatException;

@Service
public class UserService extends BaseService {

    @Resource
    private IUserDao userDao;
    @Resource
    private IGroupDao groupDao;
    @Resource
    private PasswordEncoder passwordEncoder;

    public User loadUserByUsername(String username) {
        Assert.isNotNull(username, "username is null");
        User user = this.userDao.findUserByName(username);
        return user;
    }

    public User loadUserByEmail(String email) {
        Assert.isNotNull(email, "email is null");
        User user = this.userDao.findUserByEmail(email);
        return user;
    }

    public User loadUserByMobile(String mobile) {
        Assert.isNotNull(mobile, "mobile is null");
        User user = this.userDao.findUserByEmail(mobile);
        return user;
    }

    public List<User> loadUserByNameOrCodeFuzzy(String nameOrCode) {
        Assert.isNotNull(nameOrCode, "nameOrCode is null");
        return this.userDao.findUserByNameOrCode(nameOrCode);
    }

    public User loadUser(String userId) {
        Assert.isNotNull(userId, "userId is null");
        return this.userDao.load(userId);
    }

    public String save(User user) {
        BeanTool.clearEmptyProperty(user);
        Assert.isNotNull(user, "user is null");
        this.validUser(user);

        this.validNewUser(user);

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setSignUpTime(new Date());

        return (String) this.userDao.save(user);
    }

    public PageResponse<User> findUserByPage(PageQueryRequest pageQueryRequest) {
        return this.userDao.findEntityInPage(pageQueryRequest);
    }

    /**
     * 更新除密码外的用户信息,注意用户权限，用户组等的处理，如不指定这些字段，原有字段会被删掉。
     * 
     * @param user
     *            更新用户实体，不需要指定password字段，指定也会无效
     * @throws UserNotFoundException
     *             被更新的用户不存在
     */

    public void updateUser(User user) throws UserNotFoundException {
        BeanTool.clearEmptyProperty(user);
        Assert.isNotNull(user.getId(), "userid is null");

        User existedUser = this.userDao.load(user.getId());

        if (existedUser == null)
            throw new UserNotFoundException("User id is \"" + user.getId() + "\" not found.");

        this.validUser(user);

        user.setPassword(existedUser.getPassword());
        user.setSignUpTime(existedUser.getSignUpTime());

        this.userDao.update(user);

    }

    /**
     * 修改密码，需要验证旧密码
     * 
     * @param userId
     *            被修改的用户ID
     * @param oldPassword
     *            旧密码
     * @param newPassword
     *            新密码
     * @throws UserNotFoundException
     *             用户不存在
     */
    public void updateUserPassword(String userId, String oldPassword, String newPassword) throws UserNotFoundException {
        User user = this.loadUser(userId);

        if (user == null)
            throw new UserNotFoundException("User id is \"" + userId + "\" not found.");

        if (!this.passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        String password = newPassword.trim();
        this.validPassword(password);

        user.setPassword(this.passwordEncoder.encode(password));

        this.userDao.update(user);
    }

    /**
     * 修改密码，不校验旧密码
     * 
     * @param userId
     *            被修改的用户ID
     * @param newPassword
     *            新密码
     * @throws UserNotFoundException
     *             用户不存在
     */
    public void updateUserPasswordWithoutCheck(String userId, String newPassword) throws UserNotFoundException {
        User user = this.loadUser(userId);

        if (user == null)
            throw new UserNotFoundException("User id is \"" + userId + "\" not found.");

        String password = newPassword.trim();
        this.validPassword(password);

        user.setPassword(this.passwordEncoder.encode(password));

        this.userDao.update(user);

    }

    private void validUser(User user) {
        if (user.getUsername() == null) {
            throw new NullUserNameException();
        }
        if (user.getEmail() == null) {
            throw new NullUserEmailException();
        }
        if (user.getPassword() == null) {
            throw new NullPasswordException();
        }
        // if(!(user.getMobile())) {}
        // "Mobile is null"));

        if (!(user.getUsername().matches(WebConfig.getUsernameFormat()))) {
            throw new IncorrectUsernameFormatException();
        }
        if (!(user.getEmail().matches(WebConfig.getEmailFormat()))) {
            throw new IncorrectUserEmailFormatException();
        }
    }

    private void validNewUser(User user) {

        if (this.loadUserByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyUsedFormatException();
        }
        if (this.loadUserByEmail(user.getEmail()) != null) {
            throw new UserEmailAlreadyUsedFormatException();

        }

        if (user.getMobile() != null) {
            if (this.loadUserByMobile(user.getMobile()) != null) {
                throw new UserMobileAlreadyUsedFormatException();
            }

        }

        String password = user.getPassword().trim();
        this.validPassword(password);
    }

    private void validPassword(String password) {
        if (!password.matches(WebConfig.getPasswordFormat())) {
            throw new IncorrectPasswordFormatException();
        }
        if (!(password.length() >= WebConfig.getMinPasswordLength()
                && password.length() <= WebConfig.getMaxPasswordLength())) {
            throw new IncorrectPasswordLengthException();
        }
    }
}