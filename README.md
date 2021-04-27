# BajaApp
Description:

User Instructions:
The application has been connected to firebase through its .json file and the
unique API key, if you change the database that you pull from then you will have
to change these two resources; I do not recommend this. The more simple solution
to changing where the data is pulled from is to change the session name in the
Realtime Database. This is done by changing the name on the end you push from and
then changing the "val reference" DatabaseReference in DashboardActity.kt in the
pullData function; the current path is "Baja_Data/2-push".
If you want to add more metrics to track you need to first create the variables
from your front end data pushing system and then add them to Metrics.kt in the
Metrics data class. The name you use to push the data up must exactly match the
val name in Metrics; this is case sensitive and I would avoid using spaces or
restricted variable names. After you add a val to metrics you then can just
replicate what has been done for all the other data points in the dashboard
.kt and .xml. The next variables that would be most useful and the coordinates
because they can be used in the MapsActivity.kt to draw the route that the vehicle
takes using the LatLng and the PolyLine function.
