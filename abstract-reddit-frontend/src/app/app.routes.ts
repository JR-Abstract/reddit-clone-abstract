import {RouterModule, Routes} from "@angular/router";
import {PostViewComponent} from "./view/post-view/post-view.component";
import {HomeViewComponent} from "./view/home-view/home-view.component";
import {LoginComponent} from "./auth/login/login.component";

const routes: Routes = [

  { path: '', component: HomeViewComponent },
  {
    path: 'post/:id',
    component: PostViewComponent,
    title: 'Post View'
  },
  {
    path: 'auth/login',
    component: LoginComponent,
    title: 'Login'
  },

];

export default routes;

