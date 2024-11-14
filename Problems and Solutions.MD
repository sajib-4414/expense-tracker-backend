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

#### JSON serialization
handled by jackson, no annotation of json needed, handled by jackson. but make sure you have
getter and setter in the class which you are using for json input, output.

#### spring data validation
have your classes use objects wherever possible to trigger the validation
```java
@Data
public class BudgetItemDTO {
    @NotNull(message = "category_id is required")
    private Integer category_id;
```
if you have `int` here then you cant check empty value
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

#### Manual Pagination if you dont have paginated results/Dont have JPA
Jpa repostiroy has automatic Page<EntityModel> when you have jpa repisotuiroy interface.
but for other list when you dont have Jpa repository or all of your data
is not coming from one table, so you cannot use directly jparepository paging, you,
can construct a similar pagination response like Jpa repository with this.
```java
your controller method return type
public Page<BudgetListItemDTO>

you can create a page like jpa repository with this construct
Pageable pageable = PageRequest.of(0,5);
// so you just pass a List, pageable instance, and total elements.
//your response will be consisent with like Jparepository's paginated response
return new PageImpl<>(budgetSummaryList, pageable, budgetPage.getTotalElements());
```


### Miscelleneous problem solve.
* Primary key is coming like 400,450,500 why?
  _Solve_: use generation strategy=identity in entity model. That will use incremental number.
* Returning a custom query result as json, how? also how to solve **constructor** not found when returning json response for custom query.

Solution:

    we have a custom result here, to serialize it i have a class also
```java
public List<CategoryExpense> getTop5CategoryExpenseOfMonth(int month, int user_id){
        TypedQuery<CategoryExpense> query = entityManager.createQuery(
                "select new com.sajib_4414.expense.tracker.payload.CategoryExpense(ec.id, ec.name, sum(ex.cost)) " +
                        "from Expense ex LEFT JOIN ex.category ec " +
                        "where ex.owner.id = :user_id " +
                        "and EXTRACT(month from ex.dateTime) = :month " +
                        "group by ec.id order by sum(ex.cost) desc",
                CategoryExpense.class
        );
        query.setParameter("user_id", user_id);
        query.setParameter("month", month);
        query.setMaxResults(5);

        return query.getResultList();
    }
```

custom json class look like,
the below is a pojo class used by jackson, all jackson wants is a
getter setter for all fields. used internally. anytime you want to
use a class tobe used as json input, output, make sure you have  getter setter.
but we are also using the class to get data from database where we used the constructor in the query,
so we need an appropriate constructor as well. Also important thing, data type must match
in this case. In my case one column from database, the cost column was bigint, i was having int in the
categoryresponse class, but it did not work, i had to choose Long which is compatible with
bigint coming from DB. because of that i was having error **constructor not** found. because we are doing
**constructor expression** here.
```java
@Data
@AllArgsConstructor
public class CategoryExpense {
    private final int category_id;
    private final String category_name;
    private final Long category_cost;

}
```


## Java learning TODO
* stream methods, map, filter
* query dsl
* rabbitmq using/ spring boot own mq
* spring cloud
* JTA