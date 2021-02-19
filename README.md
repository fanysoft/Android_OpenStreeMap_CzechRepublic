# Android_OpenStreeMap_CzechRepublic

Android demo with OpenStreetMap showing 14 regions of CzechRep as folows

Středočeský kraj, Praha, Jihočeský kraj, Kraj Vysočina, Plzeňský kraj, Karlovarský kraj, Ústecký kraj, Liberecký kraj, Hradecký kraj, Pardubický kraj, Olomoucký kraj, Moravskoslezský kraj, Jihomoravský kraj, Zlínský kraj

Using 
- OSMdroid lib https://github.com/osmdroid/osmdroid
- OSMbonusPack lib - https://github.com/MKergall/osmbonuspack
- DouglasPeucker reducer to reduce geoJSON

Data : geoJSON data are stores in /app/assets

    1.Original folder - original data - how to ge it
            a] find your object at https://www.openstreetmap.org
            b] copy its relation ID (as example 438344)
            c] past it to http://polygons.openstreetmap.fr/index.py 
            d] download GeoJSON file
            such files for my above regions has x10-thousands points for each vector
         
    2.OriginalRaw folder - ad1 with removed header
    
    3.Reduced folder - ad2 passed into ReduceJSON method to reduce size
                     once reduced - files has x1-thousands points for each vector
             
    4.ReducedRaw folder - ad3 with removed header
    
    
screenshot
http://www.vancura.cz/programing/Android/Demo/OpenStreetMap/1.png
