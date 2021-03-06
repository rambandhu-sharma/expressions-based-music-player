package com.example.ebmplayer.ui.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ebmplayer.R;
import com.example.ebmplayer.adapters.SongAdapter;
import com.example.ebmplayer.model.Song;
import com.example.ebmplayer.ui.activities.MainActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "cancion.playerfragment";

    private ListView songsListsView;
    private ArrayList<Song> songs;
    private SongAdapter songAdapter;
    private FirebaseStorage storage;
    private MediaPlayer mediaPlayer;

    @Override
    public void onClick(View v) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        songsListsView = view.findViewById(R.id.songs_listview);

        storage = FirebaseStorage.getInstance();
        mediaPlayer = new MediaPlayer();

        songsListsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StorageReference songReference = storage.getReferenceFromUrl(songs.get(position).songUrl);
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(Objects.requireNonNull(getActivity()), Uri.parse(songs.get(position).songUrl));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "IOException", Toast.LENGTH_SHORT).show();
                }

            }
        });

        songs = ((MainActivity) Objects.requireNonNull(getActivity())).currentPlaylist.songs;
        songAdapter = new SongAdapter(getActivity(), songs);
        songsListsView.setAdapter(songAdapter);
    }
}
