MOVEIT
===================
We like to move it, MOVEIT!
----

![picture alt](https://gitlab.eurecom.fr/mobserv/fall-2017/blob/develop/moveit/logo/logo.png)

Platform: Android

## Application description ##

An application for transporting packages through crowd sourcing drivers acting as movers. It is bla-bla car for packages.

MoveIt is an application for :
1) students and the larger crowd who lives in a place but need to sometimes or even frequently transfer stuff like for example a bag of winter clothes from home.
2) somebody who drives with some space in the trunk of the car and is willing to lend out that space for transfering packages.


MoveIT - Concept:
Use the empty space in cars to:
➢ have a sustainable and cheap delivery service (SENDER)
➢ earn easy money (for the driver) with no inconvenients (TRANSPORTER)

Main Features:
*	Cheaper delivery.
*	Sustainable.
*	Rating system.
*	User account system for a better experience
*	Optional insurance
*	Price estimator
*	Direct call  system
*	Fast payment
*	Secure delivery

##WorkFlow (Not all features are released in the current version):##

1. User creates an account by entering username, password and sets account type to be a SENDER or a TRANSPORTER.
2. Once user logs in to the system, 
	if he is a sender:
		* sees his past packages and their status.
			* By selecting a past package with status "Moving it", user can see the live tracking of the package on the map.
		* gets an option to add a new request.
			* Details of transport: location details of where to where, the package is to be moved 
			* Dimension and weight details of the package
			* Set an item as fragile or not and other details concerning the package.
			* Set a price that the user is willing to spend for the package. Moveit would suggest a price with a price estimator based on the package details and the distance of the transport.
		* On his menu items, along with other items such as change password etc, he also he gets an option to change the type of his account to being a TRANSPORTER
		
3.	If the user is a transporter
		* he/she sees a map around his current location.
		* They can opt to show packages on the map.
		* They can check which packages are on their route by tapping the package.
		* Once the package marker is tapped, it shows the details of the package and gives an option to call the sender of the package.
		
4.  They decide on a price after negotiating between sender's price, our estimated price and what the transporter expected.

5.	The full version of the application would have a booking engine through which the transporter and sender can negotiate on the price on the application and once both parties decide, they can finalise a package which would set the payment of the package. On confirmation we would process the payment online. We would also cross verify the trip using the transporter's proximity alerts when reaching an area under the sender's destination. For security purposes we would also allot an id, over email and SMS to both parties unique for the package.

6.	Currently while connecting the sender and transporter over phone call, we will notified them both by mentioning that the last 6 digits of the transporter's phone number is the unique id.

7. Both users can become a transporter or a sender.
	* Each one sees a list of previous orders and can change the status of the package.
	* Once the status is set to "A Moveit Deal!", the package is no longer seen on the map for other transporters.
	* Once the transporter starts his trip, the status is set as "Moving" and a live tracking of the package is made available to the sender.
	* Both the sender and transporter receives links to rate each other and the service of MoveIt.
	
8. Additionally, a sender can opt for insurance and both have a platform to send messages or call each other's phone.

## Developed application- Features developed##

1. Due to the time constraints, we had developed certain features of the application and focussed on the interaction design.
For simplicity, we did not segregate the users based on their type, and instead gave an option for all users to be a sender and transporter at the same time.
2. A user registration system is built using Firebase as the database.
3. While sending the package, the user enters the details (dimension, weight, fragile, optional insurance etc) in an interactive way. The user gets a suggested price, however, he/she can set their own price.
4. The in the transporter part the user goes on to a map with a feature to show packages.
5. Once the transporter selects a package, he sees different details of the package and can call the sender by tapping on the info window of the google map marker.
6. After the call once the sender decides on a transporter, the sender goes to his/her list of package and selects the one that is to be removed from the map.
