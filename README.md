# MovieApp
This app is about displaying movies as most popular or top rated ones and saves the favourites to the database.

First commit of this app (does not) support Loaders or services or SyncAdapters.

Important Note : you need to add an API Key in gradle.build , to get a key , follow the instructions of this link:
https://developers.themoviedb.org/3/getting-started

First commit has the following : 
1- Movies are displayed on the MainActivity in gridview and sorted by popularity or top-rated from a menu setting (overflow menu).
2- After Clicking on any of these movies, DetailFragment is launched and movie's details are displayed, details are title, release date, vote-average, in favourite or not, overview, trailers and reviews.
3- In the DetailFragment, there is a checkbox to save the movie in the favourites (Content Provider) or delete it from there.
4- In the FavouriteActivity, movies are displayed as MainActivity in a gridview and get the data from the Content Provider.
5- Two-pane UI is handled, the app works on tablets (devices which have screens larger than 640dp) as Master/detail flow, that works in both MainActivity and FavouriteActivity, Movies are displayed as a gridview on the leftside and details layout on the rightside.

12/2/2016 (Fri)
14:15 
