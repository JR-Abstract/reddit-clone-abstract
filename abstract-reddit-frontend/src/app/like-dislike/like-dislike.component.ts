import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ButtonModule} from "primeng/button";
import {ButtonGroupModule} from "primeng/buttongroup";

@Component({
  selector: 'app-like-dislike',
  standalone: true,
  imports: [
    ButtonModule,
    ButtonGroupModule
  ],
  templateUrl: './like-dislike.component.html',
  styleUrl: './like-dislike.component.css'
})
export class LikeDislikeComponent {
  @Input() count: any;
  @Input() entityId: any;
  @Output() upVote: EventEmitter<any> = new EventEmitter<any>();
  @Output() downVote: EventEmitter<any> = new EventEmitter<any>();
}
