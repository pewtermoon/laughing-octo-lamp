# Convert file from wav to flac format and add meta
inname="undercurrent"
outname="01_Undercurrent"
title="Undercurrent"
tracknumber=1
artist="Pewter Moon"
album="Weesperzijde"
date="2017-05-23"
genre="Electronic"

flac -f --verify -o ${outname}.flac \
-T "TRACKNUMBER=${tracknumber}" \
-T "TITLE=${title}" \
-T "ARTIST=${artist}" \
-T "ALBUM=${album}" \
-T "DATE=${date}" \
-T "GENRE=${genre}" \
${inname}.wav
