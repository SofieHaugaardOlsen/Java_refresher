# TODOs

**backend things**
* have some user handling such that tasks do not need full user objects in participants but just ids (x)

* better error messages for different ways user assignment can fail

* setup resource for user stuff

* setup SQL things?
    - user table  (id name role (password hash?))
        - password table only privileged access
    - task table  (id title stime etime issuer_id)
    - table corrolating tasks with participants?
    - how does needed role fit into this?



**frontend stuff**
the idea: 
1. user logs in (so database with passwords also a thing that needs to happen)
2. user can see all their assigned tasks in a timetable (week and month views)
3. user can issue new tasks 
    - seperate views for task issuers and task completers?
4. all (relevant) users can see unfilled tasks
5. qualified users can assign themselves to unfilled tasks
6. timetable should automatically load into current week
