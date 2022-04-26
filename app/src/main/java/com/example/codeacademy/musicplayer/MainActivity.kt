package com.example.codeacademy.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_layout.*
import kotlinx.android.synthetic.main.row_layout.view.*

class MainActivity : AppCompatActivity() {

    var listOfSongs = ArrayList<SongInfo>()
    var adapter:MySongAdapter ?= null
    var mediaPlayer:MediaPlayer ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        LoadSongsFromLocal()
//        LoadSongsInfo()
//
//        adapter = MySongAdapter(listOfSongs)
//
//        listViewSong.adapter = adapter

        var mySongThread = mySongThread()
        mySongThread.start()

    }

    fun LoadSongsFromLocal()
    {
        val allSongs = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        val cursor = contentResolver.query(allSongs,null,selection,null,null)

        if (cursor != null)
        {
            if (cursor.moveToFirst() == true)
            {
                do {
                    val songURL = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))

                    listOfSongs.add(SongInfo(songName,songAuthor,songURL))


                }while (cursor.moveToNext() == true)
            }
            cursor!!.close()

            adapter = MySongAdapter(listOfSongs)
            listViewSong.adapter = adapter
        }


    }

    fun LoadSongsInfo()
    {
        listOfSongs.add(SongInfo("Fire","آهنگ بی کلام شماره 1", "http://server2.code-academy.net/Fire.mp3"))
        listOfSongs.add(SongInfo("Wind","آهنگ بی کلام شماره 2", "http://server2.code-academy.net/Wind.mp3"))
    }

    inner class MySongAdapter : BaseAdapter
    {
        var myListSong = ArrayList<SongInfo>()

        constructor(myListSong:ArrayList<SongInfo>):super()
        {
            this.myListSong = myListSong
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myView =  layoutInflater.inflate(R.layout.row_layout,null)
            val song = this.myListSong[position]

            myView.textViewTitle.setText(song.Title)
            myView.textViewDesc.setText(song.Desc)

            myView.buttonPlay.setOnClickListener {

                if (myView.buttonPlay.text.equals("Stop"))
                {
                    mediaPlayer!!.stop()
                    myView.buttonPlay.text = "Play"
                }
                else
                {
                    mediaPlayer = MediaPlayer()

                    mediaPlayer!!.setDataSource(song.SongURL)
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                    seekBarSong.max = mediaPlayer!!.duration
                    myView.buttonPlay.text = "Stop"

                }


            }

            return myView
        }

        override fun getItem(position: Int): Any {
            return this.myListSong[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return this.myListSong.size
        }

    }


    inner class mySongThread() :Thread()
    {
        override fun run()
        {
            while(true)
            {
                try {
                    Thread.sleep(1000)
                }
                catch (ex:Exception)
                {

                }

                runOnUiThread {
                    if (mediaPlayer != null)
                    {
                        seekBarSong.progress = mediaPlayer!!.currentPosition
                    }
                }
            }
        }
    }
}
