import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {PostViewComponent} from "./view/post-view/post-view.component";
import {HomeViewComponent} from "./view/home-view/home-view.component";
import {FeedComponent} from "./post/feed/feed.component";


const routes: Routes = [

  { path: '', component: HomeViewComponent },
  {
    path: 'post/:id',
    component: PostViewComponent,
    title: 'Post View'
  },

];

export default routes;

