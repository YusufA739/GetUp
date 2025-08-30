import yt_dlp
yt_dlp_options = {}

video_link = input("Enter link to the video: ")
with yt_dlp.YoutubeDL(yt_dlp_options) as yt_dlp:
    yt_dlp.download([video_link])
