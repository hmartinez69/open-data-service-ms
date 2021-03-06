<template>
  <v-dialog
    v-model="dialogOpen"
    max-width="500px"
  >
    <v-card>
      <v-card-title>
        <span class="headline">Notification</span>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row
            cols="20"
            sm="6"
            md="4"
          >
            <v-text-field
              v-model="editedNotification.condition"
              label="Condition"
            />
          </v-row>
          <v-row
            cols="30"
            sm="6"
            md="4"
          >
            <v-select
              v-model="editedNotification.type"
              :items="notificationTypes"
              label="Type"
            />
          </v-row>
          <v-row
            cols="10"
            sm="6"
            md="4"
          >
            <webhook-edit
              v-if="editedNotification.type === 'WEBHOOK'"
              v-model="editedNotification"
              @validityChanged="validForm = $event"
              style="flex: 1 1 auto"
            />
            <firebase-edit
              v-if="editedNotification.type === 'FCM'"
              v-model="editedNotification"
              @validityChanged="validForm = $event"
              style="flex: 1 1 auto"
            />
            <slack-edit
              v-if="editedNotification.type === 'SLACK'"
              v-model="editedNotification"
              @validityChanged="validForm = $event"
              style="flex: 1 1 auto"
            />
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          color="primary"
          class="ma-2"
          :disabled="!validForm"
          @click="onSave()"
        >
          Save
        </v-btn>
        <v-btn
          color="error"
          class="ma-2"
          @click="closeDialog()"
        >
          Cancel
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import Component from 'vue-class-component'
import Vue from 'vue'
import NotificationConfig, {WebhookNotification} from '@/pipeline/notifications/notificationConfig'
import NotificationEditDialog from '@/pipeline/notifications/notificationEditDialog'
import { Emit } from 'vue-property-decorator'
import WebhookEdit from '@/pipeline/notifications/WebhookEdit.vue'
import FirebaseEdit from '@/pipeline/notifications/FirebaseEdit.vue'
import SlackEdit from '@/pipeline/notifications/SlackEdit.vue'

@Component({
  components: {SlackEdit, WebhookEdit, FirebaseEdit }
})
export default class PipelineNotifications extends Vue implements NotificationEditDialog {
  private validForm = false;
  @Emit('pipelineSaved')
  onPipelineSave () {
    return this.editedNotification
  }

  private notificationTypes = ['WEBHOOK', 'FCM', 'SLACK']
  private dialogOpen = false

  private defaultNotification: WebhookNotification = {
    notificationId: -1,
    condition: 'true',
    url: '',
    type: "WEBHOOK"
  }

  private editedNotification: NotificationConfig = Object.assign({}, this.defaultNotification)

  openDialog (notificationConfig?: NotificationConfig) {
    if (notificationConfig) { // edit
      this.editedNotification = Object.assign({}, notificationConfig)
    } else { // create
      this.editedNotification = Object.assign({}, this.defaultNotification)
    }
    this.dialogOpen = true
  }

  closeDialog () {
    this.editedNotification = Object.assign({}, this.defaultNotification)
    this.dialogOpen = false
  }

  onSave () {
    this.onPipelineSave()
    this.closeDialog()
  }
}

</script>
