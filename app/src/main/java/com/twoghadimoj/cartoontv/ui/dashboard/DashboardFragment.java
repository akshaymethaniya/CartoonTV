package com.twoghadimoj.cartoontv.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.twoghadimoj.cartoontv.database.DBOperations;
import com.twoghadimoj.cartoontv.enums.CARTOON_CATEGORY;
import com.twoghadimoj.cartoontv.PlayYoutubeVideo;
import com.twoghadimoj.cartoontv.R;
import com.twoghadimoj.cartoontv.helpers.YTDummyData;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        CardView oggyCardView = root.findViewById(R.id.oggyCardView);
        CardView chhotaBheemCardView = root.findViewById(R.id.chhotaBheemCardView);
        CardView mrbeanCardView = root.findViewById(R.id.mrbeanCardView);
        CardView ben10CardView = root.findViewById(R.id.ben10CardView);
        CardView motuPatluCardView = root.findViewById(R.id.motuPatluCardView);
        CardView pokemonCardView = root.findViewById(R.id.pokemonCardView);
        CardView tomAndJerryCardView = root.findViewById(R.id.tomAndJerryCardView);
        CardView prsCardView = root.findViewById(R.id.prsCardView);
        CardView brbCardView = root.findViewById(R.id.babCardView);
        CardView mightyRajuCardView = root.findViewById(R.id.mightyRajuCardView);
        CardView scoobydoCardView = root.findViewById(R.id.scoobydooCardView);

        setOnClickListerner(oggyCardView, CARTOON_CATEGORY.OGGY_AND_COCKRACHES.getCategoryName());
        setOnClickListerner(chhotaBheemCardView, CARTOON_CATEGORY.CHHOTA_BHEEM.getCategoryName());
        setOnClickListerner(mrbeanCardView, CARTOON_CATEGORY.MR_BEAN.getCategoryName());
        setOnClickListerner(ben10CardView, CARTOON_CATEGORY.BEN_10.getCategoryName());
        setOnClickListerner(motuPatluCardView, CARTOON_CATEGORY.MOTU_PATLU.getCategoryName());
        setOnClickListerner(pokemonCardView,CARTOON_CATEGORY.POKEMON.getCategoryName());
        setOnClickListerner(tomAndJerryCardView,CARTOON_CATEGORY.TOM_AND_JERRY.getCategoryName());
        setOnClickListerner(prsCardView,CARTOON_CATEGORY.POWER_RANGERS_SPD.getCategoryName());
        setOnClickListerner(brbCardView,CARTOON_CATEGORY.BANDBUDH_AUR_BUDBAK.getCategoryName());
        setOnClickListerner(mightyRajuCardView,CARTOON_CATEGORY.MIGHTY_RAJU.getCategoryName());
        setOnClickListerner(scoobydoCardView,CARTOON_CATEGORY.SCOOBY_DOO.getCategoryName());

        return root;
    }
    private void setOnClickListerner(CardView cardView,String category){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlayYoutubeVideo.class);
//                intent.putExtra("videoItem",new YTDummyData().getFirstVideoForCategory(getContext(),category));
                ArrayList<YoutubeVideoModel> youtubeVideoModel = new DBOperations(getContext()).getAllVideosOfCategory(category);
                if(youtubeVideoModel.size() == 0){
                    Toast.makeText(getContext(),"No Videos.",Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("videoItem",youtubeVideoModel.get(0));
                intent.putExtra("showVideosOfSameCategory",true);
                startActivity(intent);
            }
        });
    }
}