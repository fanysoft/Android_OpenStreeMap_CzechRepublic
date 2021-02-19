# Android_OpenStreeMap_CzechRepublic

Android demo with OpenStreetMap showing 14 regions of CzechRep as folows
Středočeský kraj, Praha, Jihočeský kraj, Kraj Vysočina, Plzeňský kraj, Karlovarský kraj, Ústecký kraj, Liberecký kraj, Hradecký kraj, Pardubický kraj, Olomoucký kraj, Moravskoslezský kraj, Jihomoravský kraj, Zlínský kraj

Using DouglasPeucker reducer to reduce geoJSON

Note : geoJSON data are stores in /app/assets

    1.Original folder - downloaded from http://polygons.openstreetmap.fr/index.py - insert Openstreet map Id for object you are looking for
            files has  x10-thousands points for each vector
            
    2.OriginalRaw folder - ad1 removed header
    
    3.Reduced folder - ad2 passed into ReduceJSON method to reduce size
                     once reduced - files has x1-thousands points for each vector
             
    4.ReducedRaw folder - ad3 removed header
    
    
screenshot
http://www.vancura.cz/programing/Android/Demo/OpenStreetMap/1.png
