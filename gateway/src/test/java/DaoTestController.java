
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;

import com.lgcsoft.gateway.SpringBootZuulApplication;
import com.lgcsoft.gateway.dao.gateWayUserDao;
import com.lgcsoft.gateway.entity.GateWayUser;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringBootZuulApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RestController
public class DaoTestController {
	@Autowired
	private gateWayUserDao userDao;
	/**
	 * 新增用户
	 */
	@Test
	@Ignore
	@Rollback(false)
	public void testInsertUser() {
		GateWayUser user = new GateWayUser();
		user.setName("Bob");
		user.setDepartmentId(2);
		user.setCreateDate(new Date());

		int result = this.userDao.insertUser(user);
		System.out.println("testInsertUser::" + result);
	}

	/**
	 * 通过id查找单个用户
	 */
	@Test
	@Ignore
	public void testGetUserById() {
		GateWayUser user = this.userDao.getUserById(1);
		System.out.println(user.getName());
	}

	/**
	 * 通过id修改单个用户
	 */
	@Test
	@Ignore
	public void testUpdateUserById() {
		GateWayUser user = new GateWayUser();
		user.setId(1);
		user.setName("Deft");
		user.setDepartmentId(2);
		this.userDao.updateUserById(user);
	}

	/**
	 * 通过id删除单个用户
	 */
	@Test
	@Ignore
	public void testDeleteUserById() {
		int result = this.userDao.deleteUserById(1);
		System.out.println(result);
	}

	/**
	 * 通过部门id查找多个用户
	 */
	@Test
	@Ignore
	public void testGetUserByDepartmentId() {
		List<GateWayUser> userList = this.userDao.getUserByDepartmentId(1);
		System.out.println(userList.size());
	}

	/**
	 * 使用MapSqlParameterSource
	 */
	@Test
	@Ignore
	public void testGetUserCountByDepartmentId() {
		Integer userCount = this.userDao.getUserCountByDepartmentId(1);
		System.out.println(userCount);
	}

	/**
	 * 使用MapSqlParameterSource
	 */
	@Test
	@Ignore
	public void testNewUpdateUserById() {
		GateWayUser user = new GateWayUser();
		user.setId(1);
		user.setName("Rain");
		user.setDepartmentId(2);
		Integer count = this.userDao.newUpdateUserById(user);
		System.out.println(count);
	}
}
