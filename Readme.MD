### Spring security related:
- **All unhandled exceptions are 403**: after adding spring security, no matter what exception 
happens, spring security is returning 403 for everything.
although i can see the errro in terminal. the only 
solution is using an exception handler to intercept 
and return a customized error. although i am using 
spring validation plugin, yet all 400,422 are wrapped with 
403 by spring security.
Solution: add the /error path in permitAll. then you see the errors
like all mvc frameworks, you can catch or not, upto you.
- **How to catch and handle the errors that happen in Filter(middleware)**: WHen writing JWT
filter i have seen, if there is any exception that happens in the filter which runs before
controller is even hit, then the exception handler does not work. Not in controller or in global
controller advice at all. To solve that, handle the error inside the filter, like try catch
and if you want tot return a custom json response, do it inside that. Example is in my JWT filter.
- **Cannot customize the 403 exception JSON response**: when there is no role or authority match, the
error response, I am not able to customize that yet. i tried handling with AccessDenied bean, assigning
it in the httpsecurity config, does not work so far.


### Exception handling:
- **Exceptions I learnt**:
    * NoResourceFoundException: i think this is thrown by authentiationManager when there is no user by such credentials
    * BadCredentialsException: when authentication manager sees, username or password is not correct.
    * HttpRequestMethodNotSupportedException: when GET is requested but PUT is defined.
    * DataIntegrityViolationException: when postgres/(i think other db alsso) faces error like duplicate key or something. many postgres occasion throws this error.
    * MethodArgumentNotValidException: when some mandatory params are not given in payload according to payload class @NotNull or @NotEmpty, and I am using spring-boot-validation.
    * HttpMessageNotReadableException: entire payload is missing?No payload at all.
### Entity Framework relations learning(e.g. one to one, one to many)

#### Understanding CASCADE TYPE
- you need to put CASCADETYPE on the parent, NOT on the child.
```java

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

Put **cascade type=Remove** ONLY ONLY one that field that you want to remove when you
delete an object.

Same goes for PERSIST, if you want a field to be saved(like another object)
then PUT PERSIST on it. if you are saving that field manually, then you dont
use PERSIST.

also there are REFRESH.

##### Orphan Removal:

When you use orphan removal in JPA with orphanRemoval = true, any time you remove a child entity from the parent’s collection (for example, by using the remove() method on a list), that child entity will be automatically deleted from the database.

How It Works
```
in Post class.
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Comment> comments = new ArrayList<>();
```
Now When you modify the children list by removing a child, you’re telling JPA that this child is no 
longer associated with the parent.
```
Post post = entityManager.find(Post.class, postId);
Comment comment = ...; // Assume this is a comment you want to remove
post.getComments().remove(comment); // Remove from the collection
```
After you call remove() on the list, if you persist or merge the parent entity,
JPA will automatically **delete the childToRemove entity** from the database. This could be an 
unexpected behavior, sometime you just want to detach it. have to see how to configure that
detachment.

#### Understanding JPA:
* **Use Entity Table names in the entity Query**: dont use the actual table name. for
example, we are not using e_user
```
 .createQuery("SELECT e from Expense e JOIN e.owner u" +
                " WHERE u.username = :username", Expense.class)
```
