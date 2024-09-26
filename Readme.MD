### Spring security related:
- All unhandled exceptions are 403: after adding spring security, no matter what exception 
happens, spring security is returning 403 for everything.
although i can see the errro in terminal. the only 
solution is using an exception handler to intercept 
and return a customized error. although i am using 
spring validation plugin, yet all 400,422 are wrapped with 
403 by spring security.

### Many to One and one to Many

#### Understanding CASCADETYPE
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

Put cascade type Remove ONLY ONLY one that field that you want to remove when you
delete an object.

Same goes for PERSIST, if you want a field to be saved(like another object)
then PUT PERSIST on it. if you are saving that field manually, then you dont
use PERSIST.

also there are REFRESH.

Orphan Removal:
When you use orphan removal in JPA with orphanRemoval = true, any time you remove a child entity from the parent’s collection (for example, by using the remove() method on a list), that child entity will be automatically deleted from the database.

How It Works
Configuration: You configure orphan removal in your entity relationship like this:

java
Copy code
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Child> children = new ArrayList<>();
Removing a Child:

When you modify the children list by removing a child, you’re telling JPA that this child is no longer associated with the parent.
java
Copy code
Parent parent = entityManager.find(Parent.class, parentId);
Child childToRemove = ...; // Assume this is a child you want to remove
parent.getChildren().remove(childToRemove); // Remove from the collection
Automatic Deletion:

After you call remove() on the list, if you persist or merge the parent entity, JPA will automatically delete the childToRemove entity from the database.