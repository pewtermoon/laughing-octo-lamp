s.boot;
s.quit;
// read a soundfile
b = Buffer.read(s, Platform.resourceDir +/+ "sounds/a11wlk01.wav");

// now play it
(
x = SynthDef("tutorial-PlayBuf",{ arg out = 0, bufnum;
    Out.ar( out,
        PlayBuf.ar(1, bufnum, 1
		//	BufRateScale.kr(bufnum)
		)
    )
}).add; //.play(s,[\bufnum, b]);
)
x.free; b.free;

// Cool. It works.

(
SynthDef("tutorial-Buffer-cue",{ arg out=0,bufnum;
    Out.ar(out,
        DiskIn.ar( 1, bufnum )
    )
}).add;
)

b = Buffer.cueSoundFile(s,Platform.resourceDir +/+ "sounds/a11wlk01-44_1.aiff", 0, 1);
y = Synth.new("tutorial-Buffer-cue", [\bufnum,b], s);

b.free; y.free;

// Works too.
// watch the post window
b = Buffer.read(s, Platform.resourceDir +/+ "sounds/a11wlk01.wav");
b.bufnum;
b.numFrames;
b.numChannels;
b.sampleRate;
b.free;

//
// with an action function
// note that the vars are not immediately up-to-date
(
b = Buffer.read(s, Platform.resourceDir +/+ "sounds/a11wlk01.wav", action: { arg buffer;
    ("numFrames after update:" + buffer.numFrames).postln;
    x = { PlayBuf.ar(1, buffer, BufRateScale.kr(buffer)) }.play;
});

// Note that the next line will execute BEFORE the action function
("numFrames before update:" + b.numFrames).postln;
)
x.free; b.free;

// Recording into buffers
b = Buffer.alloc(s, s.sampleRate * 5, 1); // a 5 second 1 channel Buffer

// record for four seconds
(
x = SynthDef("tutorial-RecordBuf",{ arg out=0,bufnum=0;
    var noise;
    noise = PinkNoise.ar(0.3);    // record some PinkNoise
    RecordBuf.ar(noise, bufnum);     // by default this loops
}).play(s,[\out, 0, \bufnum, b]);
)

// free the record synth after a few seconds
x.free;

// play it back
(
SynthDef("tutorial-playback",{ arg out=0,bufnum=0;
    var playbuf;
    playbuf = PlayBuf.ar(1,bufnum);
    FreeSelfWhenDone.kr(playbuf); // frees the synth when the PlayBuf has played through once
    Out.ar(out, playbuf);
}).play(s,[\out, 0, \bufnum, b]);
)
b.free;

// mix buffer with other stuff
s.boot;
s.quit;
(
 var buf, bufloaded;

 SynthDef(\playBuf, {| out = 0, bufnum = 0, rate = 1,
       dur = 0.2, amp = 0.2, startPos = 0 |
  var scaledRate, player, env;

  env = EnvGen.kr(Env.sine(dur, amp), doneAction: 2);
  scaledRate = rate * BufRateScale.kr(bufnum);
  player = PlayBuf.ar(1, bufnum, scaledRate,
       startPos: startPos, loop:1);
  Out.ar(out, player * env)
 }).add;

 bufloaded = false;

 buf = Buffer.read(s, "sounds/a11wlk01.wav",
  action: { bufloaded = nil});

 Pseq([
  Pbind( // play this Pbind first
   \scale,  Scale.gong,
   \dur,   Pwhite(1, 3, inf) * 0.001,
   \degree,  Prand([0, 2, 4, \rest], inf),
   \amp,  0.2,
   \test,  Pfunc({ bufloaded }) // when this is nil, this Pbind ends
  ),
  Pbind( // next play this one
   \instrument, \playBuf,
   \bufnum,  Pfunc({buf.bufnum}), // use the buffer, now that we know it's ready
   \dur,  Pwhite(4, 4, 5),
   \startFrame, Pfunc({buf.numFrames.rand}),
   \amp,  1
  )
 ], 1). play
)

//////
s.boot;

(
 var buf1, syn, pink;

 SynthDef(\playBuf1, {| out = 0, bufnum = 0, rate = 1, loop = 1 |
  var scaledRate, player;
  scaledRate = rate * BufRateScale.kr(bufnum);
  player = PlayBuf.ar(1, bufnum, scaledRate, loop: loop,
       doneAction:2);
  Out.ar(out, player)
 }).add;

 SynthDef(\playBufST, {| out = 0, bufnum = 0, rate = 1, loop = 1,
       dur = 1, amp = 0.2 |
  var scaledRate, player, env;

  env = EnvGen.kr(Env.sine(dur, amp), doneAction: 2);
  scaledRate = rate * BufRateScale.kr(bufnum);
  player = PlayBuf.ar(2, bufnum, scaledRate, loop:loop);
  Out.ar(out, player * env)
 }).add;

// buf1 = Buffer.read(s, "sounds/a11wlk01.wav");
buf1 = Buffer.read(s, "sounds/a11wlk01-44_1.aiff");

 Task.new({

  s.sync; // wait for buf1
  syn = Synth(\playBuf1, [\out, 0, \bufnum, buf1.bufnum,
       \rate, 1, \loop, 1]);  // play buf1

  pink =  Buffer.read(s, "sounds/SinedPink.aiff",
   action: {  // run this when pink loads
    syn.set("loop", 0);
    syn = Synth(\playBufST, [\out, 0, \bufnum, pink.bufnum,
        \rate, 1, \loop, 1, \dur, 10]); // play pink
   });


 }).play
)

//// OK, that worked.
//// Bufer recording
b = Buffer.alloc(s, s.sampleRate * 3, 1)

(SynthDef(\recBuf, {| in = 1, bufnum, loop = 1|

  var input;
  input = AudioIn.ar(in);
  RecordBuf.ar(input, bufnum, recLevel: 0.7, preLevel: 0.4, loop:loop, doneAction: 2);
 }).add;
)
////
(
 var buf, syn, pb;

 SynthDef(\playBuf2, {| out = 0, bufnum = 0, rate = 1,
       dur = 5, amp = 1, startPos = 0 |
  var scaledRate, player, env;

  env = EnvGen.kr(Env.sine(dur, amp), doneAction: 2);
  scaledRate = rate * BufRateScale.kr(bufnum);
  player = PlayBuf.ar(1, bufnum, scaledRate,
       startPos: startPos, loop:1);
  Out.ar(out, player * env)
 }).add;

 SynthDef(\recBuf, {| in = 1, bufnum, loop = 1|

  var input;
  input = AudioIn.ar(in);
  RecordBuf.ar(input, bufnum, recLevel: 0.7, preLevel: 0.4, loop:loop, doneAction: 2);
 }).add;

 buf = Buffer.alloc(s, s.sampleRate * 3, 1);

 Task({

  s.sync; // wait for the buffer

  syn = Synth(\recBuf, [\in, 1, \bufnum, buf.bufnum, \loop, 1]); // start recording

  2.wait; // let some stuff get recorded;


  pb = Pbind( // play from the same buffer
   \instrument, \playBuf2,
   \bufnum,  buf.bufnum,
   \dur,  Pwhite(0.01, 0.5, 2000),
   \startFrame, Pwhite(0, buf.numFrames.rand, inf),
   \amp,  1
  ).play;

  5.wait;

  syn.set("loop", 0);  // stop looping the recorder

  3.wait;
  pb.stop; // stop playing back
 }).play
)
