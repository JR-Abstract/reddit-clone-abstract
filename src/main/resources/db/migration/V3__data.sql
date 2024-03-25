TRUNCATE TABLE "user" RESTART IDENTITY CASCADE;
ALTER SEQUENCE user_id_seq RESTART WITH 1;

TRUNCATE TABLE subreddit RESTART IDENTITY CASCADE;
ALTER SEQUENCE subreddit_id_seq RESTART WITH 1;

TRUNCATE TABLE post RESTART IDENTITY CASCADE;
ALTER SEQUENCE post_id_seq RESTART WITH 1;

TRUNCATE TABLE comment RESTART IDENTITY CASCADE;
ALTER SEQUENCE comment_id_seq RESTART WITH 1;

TRUNCATE TABLE comment RESTART IDENTITY CASCADE;
ALTER SEQUENCE comment_id_seq RESTART WITH 1;

INSERT INTO "user" (created, email, enabled, password, username)
VALUES ('2024-03-19 08:00:00', 'java_juggernaut@example.com', TRUE, 'hashed_password', 'JavaJuggernaut'),
       ('2024-03-20 09:00:00', 'python_pirate@example.com', TRUE, 'hashed_password', 'PythonPirate'),
       ('2024-03-21 10:00:00', 'csharp_crusader@example.com', TRUE, 'hashed_password', 'CsharpCrusader'),
       ('2024-03-22 11:00:00', 'ruby_raider@example.com', TRUE, 'hashed_password', 'RubyRaider'),
       ('2024-03-23 12:00:00', 'javascript_jester@example.com', TRUE, 'hashed_password', 'JavaScriptJester'),
       ('2024-03-24 13:00:00', 'html_huntsman@example.com', TRUE, 'hashed_password', 'HTMLHuntsman'),
       ('2024-03-25 14:00:00', 'css_sorcerer@example.com', TRUE, 'hashed_password', 'CSSSorcerer')
;

INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM "user" u, role r
WHERE r.name = 'ROLE_USER';

INSERT INTO subreddit (created_date, description, name, user_id)
VALUES ('2024-03-25 15:00:00', 'A place to discuss all things Java.', 'JavaJungle', 1),
       ('2024-03-25 15:30:00', 'Explorations in the Python programming language.', 'PythonPit', 2),
       ('2024-03-25 16:00:00', 'For lovers of C# and .NET.', 'CsharpCastle', 3),
       ('2024-03-25 16:30:00', 'Delve into the world of Ruby programming.', 'RubyRealm', 4),
       ('2024-03-25 17:00:00', 'JavaScript, its frameworks, and more.', 'JavaScriptJunction', 5),
       ('2024-03-25 17:30:00', 'Discussing HTML5, semantics, and markup.', 'HTMLHaven', 6),
       ('2024-03-25 18:00:00', 'All about CSS, styling, and design.', 'CSSConclave', 7)
;

INSERT INTO post (created_date, description, post_name, url, vote_count, subreddit_id, user_id)
VALUES ('2024-03-25 19:00:00', 'How I survived a NullPointerException and other scary stories.', 'NullPointerException Nightmares',
        'https://example.com/post1', 42, 1, 1),
       ('2024-03-25 19:15:00', 'When your code runs on the first try.', 'Suspicious Success', 'https://example.com/post2', 150, 1, 2),
       ('2024-03-25 19:30:00', 'Indentation matters, and here is a horror story to prove it.', 'Indentation Indenture', 'https://example.com/post3',
        75, 2, 2),
       ('2024-03-25 19:45:00', 'I used recursion to solve a problem and now I can’t get out of it.', 'Recursive Recourse',
        'https://example.com/post4',
        90, 2, 3),
       ('2024-03-25 20:00:00', 'Exploring how to write C# code that writes C# code.', 'Csharp Inception', 'https://example.com/post5', 120, 3, 3),
       ('2024-03-25 20:15:00', 'How I built a castle in the Cloud with .NET.', 'Cloud Castle Construction', 'https://example.com/post6', 30, 3, 4),
       ('2024-03-25 20:30:00', 'When your Ruby code is a gem but nobody sees it.', 'Unseen Ruby Gems', 'https://example.com/post7', 52, 4, 4),
       ('2024-03-25 20:45:00', 'Pro tips for when your Ruby on Rails project goes off the rails.', 'Ruby Rescue Routines',
        'https://example.com/post8',
        65, 4, 5),
       ('2024-03-25 21:00:00', '10 JavaScript functions you wish you knew earlier.', 'JavaScript Jewels', 'https://example.com/post9', 200, 5, 5),
       ('2024-03-25 21:15:00', 'How to explain closures in JavaScript to a five-year-old.', 'Closure Chronicles', 'https://example.com/post10', 123, 5,
        6),
       ('2024-03-25 21:30:00', 'The ultimate debate: tabs vs. spaces in HTML.', 'The Great Space Debate', 'https://example.com/post11', 77, 6, 6),
       ('2024-03-25 21:45:00', 'Flexbox and Grid: The superhero duo of CSS.', 'Flexbox & Grid Adventures', 'https://example.com/post12', 85, 7, 7),
       ('2024-03-25 22:00:00', 'Creating a CSS-only game because why not?', 'CSS Challenge: Game On', 'https://example.com/post13', 92, 7, 1),

       ('2024-03-25 22:15:00', 'The story of how I over-engineered Hello World.', 'Overengineered Hello Java', 'https://example.com/post14', 105, 1, 1),
       ('2024-03-25 22:30:00', 'Garbage Collection: Not just an urban service!', 'Java Garbage Tales', 'https://example.com/post15', 95, 1, 1),
       ('2024-03-25 22:45:00', 'S.O.L.I.D. as a rock: Writing sturdy Java code.', 'SOLID Java Foundations', 'https://example.com/post16', 80, 1, 1),
       ('2024-03-25 23:00:00', 'Concurrent programming: How to juggle threads without getting concussed.', 'Java Thread Juggling',
        'https://example.com/post17', 122, 1, 1),
       ('2024-03-25 23:15:00', 'Anonymous classes and lambdas: The masked crusaders of Java.', 'Java’s Masked Crusaders',
        'https://example.com/post18', 111, 1, 1),
       ('2024-03-25 23:30:00', 'Reflection in Java: Mirror, mirror on the wall.', 'Reflective Java Mirrors', 'https://example.com/post19', 97, 1, 1),
       ('2024-03-25 23:45:00', 'Java 8 Streams: Fishing in the data lake.', 'Stream Fishing in Java 8', 'https://example.com/post20', 89, 1, 1)
;

INSERT INTO comment (created_date, text, post_id, user_id)
VALUES ('2024-03-25 22:15:00', 'Wait until you find out about the finally block!', 1, 2),
       ('2024-03-25 22:30:00', 'First time? Rookie numbers! You need to pump up those fails.', 1, 3),
       ('2024-03-25 22:45:00', 'That’s too suspicious... Did you try turning it off and on again?', 2, 1),
       ('2024-03-25 23:00:00', 'Indentation is the spice of life... of Python life, that is.', 3, 4),
       ('2024-03-25 23:15:00', 'It’s not a bug, it’s a feature in the next sprint.', 3, 5),
       ('2024-03-25 23:30:00', 'You are just stuck IN a LOOP. Try SOME stack overflow!', 4, 6),
       ('2024-03-25 23:45:00', 'I hear IF you go deep enough, you’ll find the answer TO life itself.', 4, 7),
       ('2024-03-26 00:00:00', 'Just wait UNTIL you START dreaming IN C#. It’s the .NET that never ends!', 5, 1),
       ('2024-03-26 00:15:00', 'Been there, built that. Next, please make it serverless!', 5, 2),
       ('2024-03-26 00:30:00', 'Will it be cloud-shaped? Asking FOR a friend.', 5, 3),
       ('2024-03-25 19:05:00', 'I once wrote a recursion so deep, I started getting StackOverflow job offers.', 1, 2),
       ('2024-03-25 19:20:00', 'If it worked on the first try, you obviously did not write enough tests.', 2, 3),
       ('2024-03-25 19:35:00', 'My code is indented so well, it’s basically modern art.', 3, 4),
       ('2024-03-25 19:50:00', 'I’ve hit the base case! I repeat, I’ve finally hit the base case!', 4, 5),
       ('2024-03-25 20:05:00', 'So you’re telling me I can use C# to write more C#? It’s like a self-sustaining ecosystem!', 5, 6),
       ('2024-03-25 20:20:00', 'Is it a bird? Is it a plane? No, it’s my serverless functions!', 6, 7),
       ('2024-03-25 20:35:00', 'A gem in the rough is still a gem. Keep shining!', 7, 1),
       ('2024-03-25 20:50:00', 'Rails is off the rails? Have you tried turning it off and on again?', 8, 2),
       ('2024-03-25 21:05:00', 'Why did I not know about these JavaScript functions during my last project?!', 9, 3),
       ('2024-03-25 21:20:00', 'Try explaining closures in JavaScript to a five-year-old and suddenly you don’t understand them either.', 10, 4),
       ('2024-03-25 21:35:00', 'Tabs vs. Spaces: An epic saga continues. I popcorn.', 11, 5),
       ('2024-03-25 21:50:00', 'Flexbox and Grid are the Batman and Robin of CSS.', 12, 6),
       ('2024-03-25 22:05:00', 'CSS-only game? Next, you’ll tell me you can make coffee with CSS.', 13, 7),
       ('2024-03-25 22:15:00', 'I showed this to my kid and now they won’t stop asking me about JavaScript.', 10, 1),
       ('2024-03-25 22:30:00', 'This is the content I signed up for. More of this please!', 10, 2),
       ('2024-03-25 22:45:00', 'I used one of these functions and now I feel like a wizard!', 10, 3),
       ('2024-03-25 23:00:00', 'Every time I think I understand closures, I realize I don’t.', 11, 4),
       ('2024-03-25 23:15:00', 'Can we get a post on explaining this to cats next?', 11, 5),
       ('2024-03-25 23:30:00', 'This is why I love JavaScript. It is full of surprises!', 11, 6),
       ('2024-03-26 08:00:00', 'Spaces forever! Fight me.', 12, 7),
       ('2024-03-26 08:15:00', 'Tabs are clearly superior, less is more!', 12, 1),
       ('2024-03-26 08:30:00', 'Why not both? Let’s not start a war.', 12, 2),
       ('2024-03-26 08:45:00', 'Tried this with my team, and now they think I’m a superhero.', 13, 3),
       ('2024-03-26 09:00:00', 'Is it possible to play this game on a potato PC?', 13, 4),
       ('2024-03-26 09:15:00', 'CSS games are the new trend, let’s make more!', 13, 5)