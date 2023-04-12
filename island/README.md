# Assignment A3: Island Generation

- Davis Lenover [lenoverd@mcmaster.ca]
- Jasmine Wang [wangj500@mcmaster.ca]
- Michael Baskaran [baskam1@mcmaster.ca]

### A4 detailed is below A3. Please scroll to bottom to read more about A4.
### Generation
To generate an island:
1) Generate a mesh using generator.jar (It is recommended that you generate an irregular mesh with at least 350 polygons)
2) Run island.jar with command arguments below (Note this list of commands includes A4, for what commands are specific to A4, please scroll to A4 section below)
   It takes in multiple arguments to generate a desired mesh:
   | Argument name | Command Abbreviation | Command | Description | Additional Notes |
   |:--:|--|---------|-------|-----|
   | Help | -h |   --help   |   Prints all available commands to console | Exits the program after execution. Provides extra information such as default values for specified arguments |
   | Mesh File Input | -i |   --input   |   Specify the mesh name (including .mesh) in your file system to read | If specifying a .mesh file from the same directory as island.jar, no file path is needed|
   | Island mesh File Output | -o |   --output   |   Specify the mesh name (including .mesh) in your file system to write an island mesh file to | If specifying a .mesh file from the same directory as island.jar, no file path is needed |
   | Island Generation Mode | -m |   --mode   |  Specify the type of island to generate | All island modes are listed by using the -h command. Normal mode is the main generation mode and lagoon will produce the MVP island |
   | Aquifer | -a |   --aquifer   | Specify the number of aquifers to be generated (as an integer) | |
   | Lakes | -l |   --lakes   | Specify the maximum number of lakes to be generated (as an integer) | This is a MAX number and strongly depends on the altitude of the island |
   | Rivers | -r |   --rivers   | Specify the maximum number of iterations for rivers to be generated (as an integer) | One pass will generate rivers where altitude difference is sufficient |
   | Seed | -s |   --seed   | Specify the seed to be used by the Normal generator |  This may drastically change how the island looks (to no island at all), it is recommended to play around with this value a lot |
   | Biome | -b |   --biomes   | Specify the biomes of island generation | Use -h to find out all biomes available |
   | Elevation (altitude) | -al |   --altitude  | Specify the altitude type of island generation | Use -h to find out all elevations available |
   | Island Shape | -sh |  --shape | Specify the shape type of island generation | Use -h to find out all shapes available |
   | soilContent | -s0 |  --soil | Specify the type of Soil Profile | Use -h to find out all soil profiles available |
   | Cities | -c |  --cities | Specify the maximum number of cities to generate | A red dot specifies the entry point to a city. The darker the square, the more populated the city is. When creating a star network, oceans are not considered island land and thus paths between cities only via oceans are invalid and thus are not calculated. |
   | City Name Data Set Path | -d |  --dataset | Specify the file path containing lines of strings to use in name generation | If this field is unused then cities will generate with no names. If specifying a .txt file from the same directory as island.jar, no file path is needed |
   | n-order | -or |  --order | Specify the length of a given n-gram for city name generation | Must be greater than 0. Each possible beginning of a name will have this n-order length. If City Name Data Set Path is not provided, this field is ignored |
   | Max Name Length to Add | -le |  --length | Specify the addon length of a given name for city name generation. | Must be greater than 0. The max length of the string is (n-order + addon length). If City Name Data Set Path is not provided, this field is ignored |
3) Run visualizer.jar with the outputted mesh from island.jar in island generation mode

Ex:
```
# First, generate mesh
mosser@azrael A2 % cd generator
mosser@azrael generator % java -jar generator.jar -mt IRREGULAR -n sample -w 1000 -l 1000 -pc 800 -r 30

mosser@azrael A2 % cd ..
mosser@azrael A2 % cd island
# The following command specifies the generated mesh from generator.jar to convert to an island with 3 lakes, arctic biome, mountain elevation, the shape of a circle, a seed of 1234 and 2 river iterations
mosser@azrael island % java -jar island.jar -i ../generator/sample.mesh -o island.mesh -m normal --l 3 -b Arctic -al MOUNTAINS -sh CIRCLE -s 1234 -r 2


mosser@azrael A2 % cd ..
mosser@azrael A2 % cd visualizer
# Run visualizer on the generated island from island.jar and use the -ig command to specify island visualization
mosser@azrael visualizer % java -jar visualizer.jar ../island/island.mesh island.svg -ig
```

## A3 Product Backlog
Status: Pending (P), Started (S), Blocked (B), Done (D)

| Id | Feature title | Who? | Start | End | Status |
|:--:|---------------|------|-------|-----|--------|
| F01 |      Island.jar can read PolyMeshes from file       |   lenoverd   |    2023-03-19   |  2023-03-19    |    D    |
| F02 |      Implement basic command line interface (Apache)      |   lenoverd   |    2023-03-19   |  2023-03-19   |     D   |
| F03 |      Generate Lagoon   |   lenoverd   |   2023-03-19  |   2023-03-20  |   D  |
| F04 |      Introduce the notion of Tiles   |   lenoverd   |   2023-03-20  |   2023-03-21  |   D  |
| F05 |      Implement special island renderer for visualizer.jar  |   lenoverd   |   2023-03-21  |   2023-03-21  |   D  |
| F06 |      Introduce the notion of Island Shapes  |   lenoverd   |   2023-03-21  |   2023-03-22  |   D  |
| F07-3 |     Introduce Lake Generation |   lenoverd   |   2023-03-22  |   2023-03-23  |   D  |
| F08-3 |      Advanced command line specification |   lenoverd   |   2023-03-24  | 2023-03-26  |   D  |
| F09 |      Introduce altitude and implement altemeric profiles |   wangj500   |   2023-03-24  |  2023-03-25 |  D  |
| F10-3 |      Introduce Tiles of various Biomes |   wangj500   |   2023-03-22  |  2023-03-22 |  D  |
| F11 |      Introduce River Generation |   wangj500   |   2023-03-24  | 2023-03-24   |  D |
| F12 |      Introduced Aquifer Generation |  baskam1   |   2023-03-22  | 2023-03-23  |  D  |
| F13 |      Create SoilProfileGenerable Interface |  baskam1   |   2023-03-23  | 2023-03-23  |  D  |
| F14 |      Create SoilProfile Abstract class to help develop different SoilProfiles |  baskam1   |   2023-03-23  | 2023-03-24  |  D  |
| F15 |      Generated specific SoilProfiles for users to select from |  baskam1   |   2023-03-24  | 2023-03-26  |  D  |
| F16 |      Implemented capabilities to computeRemainingWater |  baskam1   |   2023-03-25  | 2023-03-26  |  D  |
| F17-3 (2) R2 |      Implemented unified island generator |  lenoverd   |   2023-03-25  | 2023-03-29  |  D  |

# Assignment A4: Urbanism

- Davis Lenover [lenoverd@mcmaster.ca]

## A4: Specific Commands
Alongside generating an island, one can also use the following commands when executing island.jar:
| Argument name | Command Abbreviation | Command | Description | Additional Notes |
|:--:|--|---------|-------|-----|
| Cities | -c |  --cities | Specify the maximum number of cities to generate | A red dot specifies the entry point to a city. The darker the square, the more populated the city is. When creating a star network, oceans are not considered island land and thus paths between cities only via oceans are invalid and thus are not calculated. |
| City Name Data Set Path | -d |  --dataset | Specify the file path containing lines of strings to use in name generation | If this field is unused then cities will generate with no names. If specifying a .txt file from the same directory as island.jar, no file path is needed |
| n-order | -or |  --order | Specify the length of a given n-gram for city name generation | Must be greater than 0. Each possible beginning of a name will have this n-order length. If City Name Data Set Path is not provided, this field is ignored |
| Max Name Length to Add | -le |  --length | Specify the addon length of a given name for city name generation. | Must be greater than 0. The max length of the string is (n-order + addon length). If City Name Data Set Path is not provided, this field is ignored |

## A4: Data Set Credits
cityNameSet.txt is comprised of data retrieved from the Canadian Geographical Names Database. A publicly accessible database containing names for geographical locations across Canada.
View here: https://natural-resources.canada.ca/earth-sciences/geography/download-geographical-names-data/9245

## A4: Data Set Justification
The overall idea was to support loading of different data sets. Thus, users can load any .txt file and specify the generation parameters via the command line. This also scales much better as apposed to a fixed dataset hardcoded as this would make for a very large class file.
This process was introduced as a generator in the island subproject because that's exactly what it is, it is generating names thus, it is delegated as such and can be used in any island generation classes as a tool if need be.
This also means one class carries the responsibility of the generation process and simplifies said generation interactions with users in a facade pattern manner, it simply asks for the data set and the two generation parameters. From there a string is created.

## A4: Example
```
# First, generate mesh
mosser@azrael A2 % cd generator
mosser@azrael generator % java -jar generator.jar -mt IRREGULAR -n sample -w 1000 -l 1000 -pc 800 -r 30

mosser@azrael A2 % cd ..
mosser@azrael A2 % cd island
# The following command specifies the generated mesh from generator.jar to convert to an island with 3 lakes, arctic biome, mountain elevation, the shape of a circle, a seed of 1234, 2 river iterations, maximum of ten cities with names generated from cityNameSet.txt with an order of 4 and a max length of 13 (4+9)
mosser@azrael island % java -jar island.jar -i ../generator/sample.mesh -o island.mesh -m normal -l 3 -b Arctic -al MOUNTAINS -sh CIRCLE -s 21543 -r 2 -c 10 -d cityNameSet.txt -or 4 -le 9


mosser@azrael A2 % cd ..
mosser@azrael A2 % cd visualizer
# Run visualizer on the generated island from island.jar and use the -ig command to specify island visualization
mosser@azrael visualizer % java -jar visualizer.jar ../island/island.mesh island.svg -ig
```
## A4: Visualization
![alt text](https://github.com/2AA4-W23/a4-urbanism-davislenover/blob/main/A4VISUAL.png?raw=true)
- The red dot indicates the entry point to the given polygon city
- The colour of the polygon corresponds to the type of city (where darker means higher populations)
- The String next to the red dot indicates the name of the city (if applicable)

## A4 Product Backlog
|     Id     | Feature title                                                           | Start      | End                         | Status |
|:----------:|-------------------------------------------------------------------------|------------|-----------------------------|--------|
|    4F01    | Implement the notion of a graph Node                                    | 2023-03-30 | 2023-03-30                  | D      |
|    4F02    | Implement the notion of graph node properties                           | 2023-03-30 | 2022-04-01                  | D      |
|    4F03    | Implement the notion of a graph edge                                    | 2023-04-01 | 2023-04-01                  | D      |
|    4F04    | Implement the notion of graph edge properties                           | 2023-04-01 | 2023-04-01                  | D      |
|    4F05    | Implement Graph ADT                                                     | 2023-04-01 | 2023-04-02                  | D      |
| 4F06 (_R3) | Implement Pathfinder ADT and sample algorithm                           | 2023-04-02 | 2023-04-02 (_R3 2023-04-03) | D      |
|    4F07    | Implement separate city tiles based on city size                        | 2023-04-04 | 2023-04-04                  | D      |
|    4F08    | Implement road property                                                 | 2023-04-04 | 2023-04-07                  | D      |
|    4F09    | Implement city generator                                                | 2023-04-04 | 2023-04-07                  | D      |
|    4F10    | Implement maximum number of cities via CMD                              | 2023-04-04 | 2023-04-07                  | D      |
|    4F11    | Implement city names via a Markov process                               | 2023-04-08 | 2023-04-10                  | D      |
|    4F12    | Document Pathfinder in a new README                                     | 2023-04-10 | 2023-04-10                  | D      |
|    4F13    | README organizational changes                                           | 2023-04-10 | 2023-04-10                  | D      |
|    4FR     | Provide documentation of realizing the pathfinder interface in projects | 2023-04-11 | 2023-04-11                  | D      |