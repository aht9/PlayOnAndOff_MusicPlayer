package com.example.codeacademy.musicplayer

class SongInfo
{
    var Title:String ?= null
    var Desc:String ?= null
    var SongURL:String ?= null

    constructor(title:String,desc:String,songURL:String)
    {
        this.Title = title
        this.Desc = desc
        this.SongURL = songURL
    }
}