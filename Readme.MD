### Spring security related:
- All unhandled exceptions are 403: after adding spring security, no matter what exception 
happens, spring security is returning 403 for everything.
although i can see the errro in terminal. the only 
solution is using an exception handler to intercept 
and return a customized error. although i am using 
spring validation plugin, yet all 400,422 are wrapped with 
403 by spring security.

#### How to use flyaway
- in build.gradle
```declarative
implementation 'org.flywaydb:flyway-core'
implementation 'org.flywaydb:flyway-database-postgresql'
```
- disable spring's default database modification functionality, so your changes
are gradual and tracked.
in application.properties file
`spring.jpa.hibernate.ddl-auto=none`
- Now, enable creating migration history folder, to do that in application.properties file
`spring.flyway.baselineOnMigrate = true`. This will create the flyaway_schema_history table
Run the project at this time to see if this table is created. Ensure
- Start writign sql. Default path is resources -> db -> migration folder.
sql file naming convension:
`VX_X_X__NAME.sql or VX__NAME.sql ` Example: `V1_0_1__creating_dummy_user.sql`
That Two **__** is a MUST
- Special Note: For project where Tables are already created, dont start with V1. 
Must start with at least V1_0_1.
### Many to One and one to Many

#### Understanding CASCADETYPE
- you need to put CASCADETYPE on the parent, NOT on the child.
```

Post{
    @OneToMany(cascade=cascadetype.REMOVE)
    commentList
}

Postcomment{
    @ManyToOne //Dont put cascade here
    Post post;
}
```
**cascadetype=remove**: the Field that has this notation, will be deleted 
when you delete an object. Here if you put that cascadetype remove in post on
the field of commentList,  then when you delete post, commentList will be deleted.
**BUT if you mistakenly do vice versa or Both**, then when you delete a comment,
your post will be deleted as well. WHICH IS A MISTAKE.

Put cascade type Remove ONLY ONLY one that field that you want to remove when you
delete an object.

Same goes for PERSIST, if you want a field to be saved(like another object)
then PUT PERSIST on it. if you are saving that field manually, then you dont
use PERSIST.

also there are REFRESH.

**Orphan Removal:**

When you use orphan removal in JPA with orphanRemoval = true, any time you remove a child entity from the parent’s collection (for example, by using the remove() method on a list), that child entity will be automatically deleted from the database.

How It Works
Configuration: You configure orphan removal in your entity relationship like this:

```java
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Child> children = new ArrayList<>();
```

Removing a Child:

When you modify the children list by removing a child, you’re telling JPA that this child is no longer associated with the parent.
```declarative
Parent parent = entityManager.find(Parent.class, parentId);
Child childToRemove = ...; // Assume this is a child you want to remove
parent.getChildren().remove(childToRemove); // Remove from the collection
```
Automatic Deletion:

After you call remove() on the list, if you persist or merge the parent entity, JPA will automatically delete the childToRemove entity from the database.


#### JSON serialization problem:
* use `Jsonignore` if you want to omit some field during serializing json output.
* use `@JsonProperty(access = JsonProperty.Access.READ_ONLY)` A read-only property is included in the JSON output (serialization) but ignored during JSON input (deserialization).
```declarative
public class User {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String username;

    // Getters and setters
}

```
* A write-only property is ignored during JSON output (serialization) but included during JSON input (deserialization).
```declarative
public class User {
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String username;

    // Getters and setters
}

```
* **To avoid circular reference and exception**, in JSON serialization 
like (User->userList->user->userList->user), use jsonignore/writeonly/readonly
or Managed reference and back reference.

`@JsonManagedReference`

Usage: This annotation is used on the parent side of the relationship.

Function: It indicates that this part of the relationship should be serialized normally.

Example: In a User entity that has a collection of UserRole entities, you would 
place `@JsonManagedReference` on the userRoles field.

```declarative
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<UserRole> userRoles = new HashSet<>();

    // other fields, getters, and setters
}

```

`@JsonBackReference`

Usage: This annotation is used on the child side of the relationship.

Function: It indicates that this part of the relationship should be ignored during serialization to prevent circular references
```declarative
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<UserRole> userRoles = new HashSet<>();

    // other fields, getters, and setters
}

```

### Miscelleneous problem solve or Problems(unsolved)
* Primary key is coming like 400,450,500 why?
_Solve_: use generation strategy=identity in entity model. That will use incremental number.
* Problem: Transactional not working when put at class level to roll back test data
in test classes and methods.

solution: use @Transactional in the test methods not class to have the rollback automatic.
* Problem: Testing does not delete data created on the database with BeforeAll /beforeClass

Explanation: Usually in test methods, if I put @Transactional, then all the data
that has been written on DB during test, will be deleted/rollbacked. but I if i want the
**beforeAll** method to do something that writes to data, for example, before all test, 
lets create a user which will be used for all test, then that setup wants tobe 
static, and inside static we cannot use autowired mockmvc at all. if we also workaround like this
```declarative

public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private String jwtToken;

    @Transactional
    @BeforeAll
    @Rollback(value = true)
    public  static void setUp(WebApplicationContext wac) throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setUsername("john");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");

        ObjectMapper privateIObjectMapper = new ObjectMapper();
        MockMvc privateMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        String jsonRequest = privateIObjectMapper.writeValueAsString(request);


        privateMVC.perform
                        (post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                        )
                .andExpect(status().isCreated());

    }
```
Transaction does not work at all here, the data is persisted. Transactional annotation
only work in @Test(test methods) or @BeforeEach methods. 

Workaround: so, dont make any api calls that create data on the database, in the BeforeALl
static method. @Transactional does not work. test data created on this @Beforeall
stays in the database and next time test fails. You have two options, either deleting 
the data created in @beforeall manually, or use @beforeach. data used in before each
are rolled back.