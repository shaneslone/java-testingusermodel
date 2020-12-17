package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class,
        properties = {"command.line.runner.enabled=false"})
public class UserServiceUnitNoDBTest {

    @Autowired
    private UserService userService;

    @MockBean
    private RoleService roleService;

   @MockBean
   private UserRepository userrepos;

   private List<User> userList = new ArrayList<>();

    @Before
    public void setUp() {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("test admin",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(10);
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r2));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r3));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));

        userList.add(u1);

        // data, user
        User u2 = new User("test cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.setUserid(20);
        u2.getRoles()
                .add(new UserRoles(u2,
                        r2));
        u2.getRoles()
                .add(new UserRoles(u2,
                        r3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        userList.add(u2);

        // user
        User u3 = new User("test barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u3.setUserid(30);
        u3.getRoles()
                .add(new UserRoles(u3,
                        r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        userList.add(u3);

        User u4 = new User("puttat",
                "password",
                "puttat@school.lambda");
        u4.setUserid(40);
        u4.getRoles()
                .add(new UserRoles(u4,
                        r2));
        userList.add(u4);

        User u5 = new User("test misskitty",
                "password",
                "misskitty@school.lambda");
        u4.setUserid(50);
        u5.getRoles()
                .add(new UserRoles(u5,
                        r2));
        userList.add(u5);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void findAll() {
        Mockito.when(userrepos.findAll())
                .thenReturn(userList);

        System.out.println(userService.findAll());

        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void findByNameContaining() {
        Mockito.when(userrepos.findByUsernameContainingIgnoreCase("barn"))
                .thenReturn(userList);
        assertEquals(5, userService.findByNameContaining("barn").size());

    }

    @Test
    public void findUserById() {
        Mockito.when(userrepos.findById(1L))
                .thenReturn(Optional.of(userList.get(0)));

        assertEquals("test admin", userService.findUserById(1L).getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findUserByIdNotFound() {
        Mockito.when(userrepos.findById(100L))
                .thenThrow(ResourceNotFoundException.class);

        assertEquals("test admin", userService.findUserById(100L).getUsername());
    }

    @Test
    public void findByName() {
        Mockito.when(userrepos.findByUsername("test admin"))
                .thenReturn(userList.get(0));
        assertEquals("test admin", userService.findByName("test admin").getUsername());
    }

    @Test
    public void delete() {
        Mockito.when(userrepos.findById(1L))
                .thenReturn(Optional.of(userList.get(0)));
        Mockito.doNothing()
                .when(userrepos)
                .deleteById(1L);
        userService.delete(1);
        assertEquals(5, userList.size());
    }

    @Test
    public void save() {
        User u1 = new User("test admin",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(0);

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));

        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));

        Mockito.when(userrepos.save(any(User.class)))
                .thenReturn(u1);
        Mockito.when(roleService.findRoleById(1L))
                .thenReturn(r1);

        User addUser = userService.save(u1);
        assertNotNull(addUser);
        assertEquals(u1.getUsername(), addUser.getUsername());

    }

    @Test
    public void update() {
        User u1 = new User("test admin",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(10);

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));

        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));
        Mockito.when(roleService.findRoleById(1L))
                .thenReturn(r1);
        Mockito.when(userrepos.findById(10L))
                .thenReturn(Optional.of(u1));
        Mockito.when(userrepos.save(any(User.class)))
                .thenReturn(u1);
        User updateUser = userService.update(u1, 10);

        assertNotNull(updateUser);
        assertEquals(updateUser, u1);

    }

    @Test
    public void deleteAll() {
    }
}