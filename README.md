# Welcome to ParkingLib Services API!

Parking Services API handles incoming and outgoing cars (of a certain type) to and from specific parking lots.

The main principles are:

A toll parking contains multiple parking slots of different types :
- standard parking slots for sedan cars (gasoline-powered)
- parking slots with 20kw power supply for electric cars
- parking slots with 50kw power supply for electric cars
Each car type must occupy its own type of parking slot.

For billing purposes, every parking is free to implement is own pricing policy :
- Some only bills their customer for each hour spent in the parking (nb hours * hour price)
- Some other bill a fixed amount + each hour spent in the parking (fixed amount + nb hours * hour price)
- It's possible to add any other types of policy by only adding the appropriate formula.

Cars of all types come in and out randomly, the API:
- Sends them to the right parking slot or warns (refuse them) if there is no slot of the right type left.
- Marks the parking slot as Free and bills the customer when the car leaves.

# How to install

 - Do a "git clone" from this repository to your local drive. For example to: D:\MyWorkSpace\ParkingLib
 - You may configure your Maven settings by using the provided "settings.xml" file.
 
> You may also use an IDE or any other tool to perform these operation.

## Configure the Parking database

The application comes with an embedded H2 database which may be changed to any other database by just modifying the appropriate entries in the "application.properties" file.
In any case, a very small data set is provided on the "data.sql" file. This data set includes "P1" and "P2" parkings with a certain capacity for each car type, and a pricing policy.

## How to build and run the application

- cd to D:\MyWorkSpace\ParkingLib\ParkingLibServices
- Run the command "mvn clean package". This will build, test and create the jar application.
- Go to the target directory: "cd target"
- Run the command "java -jar ParkingLibServices-1.0.0-SNAPSHOT.jar"

## Take a look at the API documentation

The API is documented with Swagger, and available at http://localhost:8080/swagger-ui.html
> This can be configured on the "application.properties" file: springdoc.swagger-ui.path=/swagger-ui.html

If you try any endpoint, you may use the following values:
- parkingName: "P1" or "P2" (names available at the "data.sql" file).
- carType: "sedan", "e_high", or "e_low".
- (for the /comeOut request) placeNumber: Some place number as returned by any /comeIn request. Note: the place numbers start from 0 up to the capacity value for the specified car type.

## Usage

Trying the /comeIn endpoint examples:
- http://127.0.0.1:8080/comeIn?parkingName=P1&carType=sedan
- http://127.0.0.1:8080/comeIn?parkingName=P2&carType=e_high
- http://127.0.0.1:8080/comeIn?parkingName=P1&carType=sedan

Trying the /comeOut endpoint example:
- http://127.0.0.1:8080/comeOut?parkingName=P1&carType=sedan&placeNumber=1